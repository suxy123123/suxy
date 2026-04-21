<template>
  <div class="manuscript-list">
    <el-card>
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input
            v-model="searchForm.title"
            placeholder="请输入标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(item, key) in statusMap"
              :key="key"
              :label="item.label"
              :value="key"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button type="success" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新建稿件
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        :data="tableData"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" align="center" />
        
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="updateTime" label="更新时间" width="180" align="center">
          <template #default="{ row }">
            {{ formatDate(row.updateTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleViewDetail(row.id)"
            >
              详情
            </el-button>

            <el-button
              v-if="canEdit(row)"
              type="primary"
              size="small"
              link
              @click="handleEdit(row.id)"
            >
              编辑
            </el-button>

            <el-button
              v-if="canSubmit(row)"
              type="success"
              size="small"
              link
              @click="handleSubmit(row.id)"
            >
              提交一审
            </el-button>

            <el-button
              v-if="canApprove(row)"
              type="success"
              size="small"
              link
              @click="handleApprove(row.id)"
            >
              通过
            </el-button>

            <el-button
              v-if="canReject(row)"
              type="danger"
              size="small"
              link
              @click="handleReject(row)"
            >
              退回
            </el-button>

            <el-button
              v-if="canResubmit(row)"
              type="warning"
              size="small"
              link
              @click="handleResubmit(row.id)"
            >
              重提
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 退回对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="退回稿件"
      width="500px"
    >
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="退回原因" required>
          <el-input
            v-model="rejectForm.rejectReason"
            type="textarea"
            :rows="4"
            placeholder="请输入退回原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject" :loading="rejectLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getManuscriptList, submitPending1, approve, reject, resubmit } from '@/api/manuscript'
import { statusMap, getStatusType, getStatusText, formatDate } from '@/utils/helpers'

const router = useRouter()
const loading = ref(false)
const rejectLoading = ref(false)
const rejectDialogVisible = ref(false)
const tableData = ref([])
const currentManuscript = ref(null)

// 搜索表单
const searchForm = reactive({
  title: '',
  status: ''
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 退回表单
const rejectForm = reactive({
  id: null,
  rejectReason: ''
})

// 获取用户信息
const userInfo = computed(() => {
  const user = localStorage.getItem('loginUser')
  return user ? JSON.parse(user) : null
})

// 获取用户权限列表
const userPermissions = computed(() => {
  return userInfo.value?.permissions || []
})

// 检查是否拥有某个权限
const hasPermission = (permissionKey) => {
  return userPermissions.value.includes(permissionKey)
}

// 获取稿件列表
const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getManuscriptList(params)
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchList()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.status = ''
  pagination.pageNum = 1
  fetchList()
}

// 新建稿件
const handleCreate = () => {
  router.push('/manuscript/create')
}

// 查看详情
const handleViewDetail = (id) => {
  router.push(`/manuscript/detail/${id}`)
}

// 编辑
const handleEdit = (id) => {
  router.push(`/manuscript/edit/${id}`)
}

// 提交一审
const handleSubmit = async (id) => {
  try {
    await ElMessageBox.confirm('确定要提交一审吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await submitPending1(id)
    ElMessage.success('提交成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
    }
  }
}

// 审核通过
const handleApprove = async (id) => {
  try {
    await ElMessageBox.confirm('确定要通过审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await approve(id)
    ElMessage.success('审核通过')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('审核失败:', error)
    }
  }
}

// 退回
const handleReject = (row) => {
  currentManuscript.value = row
  rejectForm.id = row.id
  rejectForm.rejectReason = ''
  rejectDialogVisible.value = true
}

// 确认退回
const confirmReject = async () => {
  if (!rejectForm.rejectReason) {
    ElMessage.warning('请输入退回原因')
    return
  }
  
  rejectLoading.value = true
  try {
    await reject(rejectForm)
    ElMessage.success('退回成功')
    rejectDialogVisible.value = false
    fetchList()
  } catch (error) {
    console.error('退回失败:', error)
  } finally {
    rejectLoading.value = false
  }
}

// 重提
const handleResubmit = async (id) => {
  try {
    await ElMessageBox.confirm('确定要重新提交吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await resubmit(id)
    ElMessage.success('重提成功')
    fetchList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重提失败:', error)
    }
  }
}

// 分页大小改变
const handleSizeChange = () => {
  fetchList()
}

// 页码改变
const handlePageChange = () => {
  fetchList()
}

// 权限判断 - 是否可以编辑
const canEdit = (row) => {
  // 拥有编辑权限且稿件处于草稿或已退回状态
  return hasPermission('manuscript:edit') && (row.status === 'DRAFT' || row.status === 'REJECTED')
}

// 权限判断 - 是否可以提交一审
const canSubmit = (row) => {
  // 拥有提交权限且稿件处于草稿或已退回状态
  return hasPermission('manuscript:submit') && (row.status === 'DRAFT' || row.status === 'REJECTED')
}

// 权限判断 - 是否可以审核通过
const canApprove = (row) => {
  const role = userInfo.value?.roleCode
  if (!role) return false
  // 管理员可以审核所有待审状态的稿件
  if (role === 'ADMIN' && row.status.startsWith('PENDING_')) {
    return true
  }
  // 一审员只能审核待一审的稿件
  if (role === 'PENDING_1' && row.status === 'PENDING_1') {
    return true
  }
  // 二审员可以审核待一审和待二审的稿件
  if (role === 'PENDING_2' && (row.status === 'PENDING_1' || row.status === 'PENDING_2')) {
    return true
  }
  // 三审员可以审核所有待审状态的稿件（权限最高）
  if (role === 'PENDING_3' && (row.status === 'PENDING_1' || row.status === 'PENDING_2' || row.status === 'PENDING_3')) {
    return true
  }
  return false
}

// 权限判断 - 是否可以退回
const canReject = (row) => {
  const role = userInfo.value?.roleCode
  if (!role) return false
  // 管理员可以退回所有待审状态的稿件
  if (role === 'ADMIN' && row.status.startsWith('PENDING_')) {
    return true
  }
  // 一审员只能退回待一审的稿件
  if (role === 'PENDING_1' && row.status === 'PENDING_1') {
    return true
  }
  // 二审员可以退回待一审和待二审的稿件
  if (role === 'PENDING_2' && (row.status === 'PENDING_1' || row.status === 'PENDING_2')) {
    return true
  }
  // 三审员可以退回所有待审状态的稿件（权限最高）
  if (role === 'PENDING_3' && (row.status === 'PENDING_1' || row.status === 'PENDING_2' || row.status === 'PENDING_3')) {
    return true
  }
  return false
}

// 权限判断 - 是否可以重提
const canResubmit = (row) => {
  // 拥有重提权限且稿件处于已退回状态
  return hasPermission('manuscript:resubmit') && row.status === 'REJECTED'
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.manuscript-list {
  height: 100%;
}

.search-form {
  margin-bottom: 20px;
}
</style>

<template>
  <div class="manuscript-detail">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>稿件详情</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <div v-if="manuscript" class="detail-content">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="ID">
            {{ manuscript.id }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(manuscript.status)">
              {{ getStatusText(manuscript.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标题" :span="2">
            {{ manuscript.title }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(manuscript.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(manuscript.updateTime) }}
          </el-descriptions-item>
          <el-descriptions-item
            v-if="manuscript.rejectReason"
            label="退回原因"
            :span="2"
          >
            <el-alert
              :title="manuscript.rejectReason"
              type="error"
              :closable="false"
              show-icon
            />
          </el-descriptions-item>
        </el-descriptions>

        <!-- 稿件内容 -->
        <el-divider content-position="left">稿件内容</el-divider>
        <div class="content-box">
          <pre class="content-text">{{ manuscript.content }}</pre>
        </div>

        <!-- 流转历史 -->
        <el-divider content-position="left">流转历史</el-divider>
        <el-timeline v-if="manuscript.reviewRecords && manuscript.reviewRecords.length > 0">
          <el-timeline-item
            v-for="(record, index) in manuscript.reviewRecords"
            :key="index"
            :timestamp="formatDate(record.operateTime)"
            placement="top"
            :type="getTimelineType(record.operation)"
          >
            <el-card>
              <h4>{{ getOperationText(record.operation) }}</h4>
              <p v-if="record.operator">操作人: {{ record.operator }}</p>
              <p v-if="record.comment">备注: {{ record.comment }}</p>
              <p v-if="record.rejectReason" class="reject-reason">
                退回原因: {{ record.rejectReason }}
              </p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无流转记录" />

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button
            v-if="canEdit"
            type="primary"
            @click="handleEdit"
          >
            编辑
          </el-button>

          <el-button
            v-if="canSubmit"
            type="success"
            @click="handleSubmit"
          >
            提交一审
          </el-button>

          <el-button
            v-if="canApprove"
            type="success"
            @click="handleApprove"
          >
            审核通过
          </el-button>

          <el-button
            v-if="canReject"
            type="danger"
            @click="handleReject"
          >
            退回
          </el-button>

          <el-button
            v-if="canResubmit"
            type="warning"
            @click="handleResubmit"
          >
            重提
          </el-button>
        </div>
      </div>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getManuscriptDetail,
  submitPending1,
  approve,
  reject,
  resubmit
} from '@/api/manuscript'
import { getStatusType, getStatusText, formatDate } from '@/utils/helpers'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const rejectLoading = ref(false)
const rejectDialogVisible = ref(false)
const manuscript = ref(null)
const currentId = ref(route.params.id)

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

// 获取详情
const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getManuscriptDetail(currentId.value)
    manuscript.value = res.data
  } catch (error) {
    console.error('获取详情失败:', error)
  } finally {
    loading.value = false
  }
}

// 返回
const goBack = () => {
  router.back()
}

// 编辑
const handleEdit = () => {
  router.push(`/manuscript/edit/${currentId.value}`)
}

// 提交一审
const handleSubmit = async () => {
  try {
    await ElMessageBox.confirm('确定要提交一审吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await submitPending1(currentId.value)
    ElMessage.success('提交成功')
    fetchDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
    }
  }
}

// 审核通过
const handleApprove = async () => {
  try {
    await ElMessageBox.confirm('确定要通过审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await approve(currentId.value)
    ElMessage.success('审核通过')
    fetchDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('审核失败:', error)
    }
  }
}

// 退回
const handleReject = () => {
  rejectForm.id = currentId.value
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
    fetchDetail()
  } catch (error) {
    console.error('退回失败:', error)
  } finally {
    rejectLoading.value = false
  }
}

// 重提
const handleResubmit = async () => {
  try {
    await ElMessageBox.confirm('确定要重新提交吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await resubmit(currentId.value)
    ElMessage.success('重提成功')
    fetchDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重提失败:', error)
    }
  }
}

// 获取时间轴类型
const getTimelineType = (operationType) => {
  const typeMap = {
    'CREATE': 'primary',
    'SUBMIT': 'success',
    'APPROVE': 'success',
    'REJECT': 'danger',
    'RESUBMIT': 'warning'
  }
  return typeMap[operationType] || 'info'
}

// 获取操作文本
const getOperationText = (operationType) => {
  const textMap = {
    'CREATE': '创建稿件',
    'SUBMIT': '提交审核',
    'APPROVE': '审核通过',
    'REJECT': '退回修改',
    'RESUBMIT': '重新提交'
  }
  return textMap[operationType] || operationType
}

// 权限判断
const canEdit = computed(() => {
  const role = userInfo.value?.roleCode
  if (!manuscript.value) return false
  if (role === 'EDITOR') {
    return manuscript.value.status === 'DRAFT' || manuscript.value.status === 'REJECTED'
  }
  return false
})

const canSubmit = computed(() => {
  const role = userInfo.value?.roleCode
  if (!manuscript.value) return false
  if (role === 'EDITOR') {
    return manuscript.value.status === 'DRAFT' || manuscript.value.status === 'REJECTED'
  }
  return false
})

const canApprove = computed(() => {
  const role = userInfo.value?.roleCode
  if (!manuscript.value) return false
  if (role === 'PENDING_1' && manuscript.value.status === 'PENDING_1') {
    return true
  }
  if (role === 'PENDING_2' && manuscript.value.status === 'PENDING_2') {
    return true
  }
  return false
})

const canReject = computed(() => {
  const role = userInfo.value?.roleCode
  if (!manuscript.value) return false
  if (role === 'PENDING_1' && manuscript.value.status === 'PENDING_1') {
    return true
  }
  if (role === 'PENDING_2' && manuscript.value.status === 'PENDING_2') {
    return true
  }
  return false
})

const canResubmit = computed(() => {
  const role = userInfo.value?.roleCode
  if (!manuscript.value) return false
  if (role === 'EDITOR') {
    return manuscript.value.status === 'REJECTED'
  }
  return false
})

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.manuscript-detail {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-content {
  padding: 10px;
}

.content-box {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  max-height: 500px;
  overflow-y: auto;
}

.content-text {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  font-family: inherit;
  line-height: 1.8;
}

.reject-reason {
  color: #f56c6c;
  margin-top: 8px;
  margin-bottom: 0;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  gap: 10px;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #e6e6e6;
}
</style>

<template>
  <div class="manuscript-edit">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑稿件' : '新建稿件' }}</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="edit-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入稿件标题"
            maxlength="200"
            show-word-limit
            clearable
          />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="20"
            placeholder="请输入稿件内容"
            maxlength="50000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ isEdit ? '保存' : '创建' }}
          </el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createManuscript, editManuscript, getManuscriptDetail } from '@/api/manuscript'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 是否为编辑模式
const isEdit = computed(() => !!route.params.id)

// 表单数据
const form = reactive({
  id: null,
  title: '',
  content: ''
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入稿件标题', trigger: 'blur' },
    { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入稿件内容', trigger: 'blur' },
    { min: 1, message: '内容不能为空', trigger: 'blur' }
  ]
}

// 获取详情（编辑模式）
const fetchDetail = async () => {
  if (!isEdit.value) return
  
  loading.value = true
  try {
    const res = await getManuscriptDetail(route.params.id)
    const data = res.data
    form.id = data.id
    form.title = data.title
    form.content = data.content
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

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          // 编辑
          await editManuscript(form)
          ElMessage.success('保存成功')
        } else {
          // 新建
          await createManuscript(form)
          ElMessage.success('创建成功')
        }
        router.push('/manuscripts')
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.manuscript-edit {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-form {
  max-width: 900px;
  margin: 0 auto;
}
</style>

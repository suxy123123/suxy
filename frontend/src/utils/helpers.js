// 稿件状态映射
export const statusMap = {
  DRAFT: { label: '草稿', type: 'info' },
  PENDING_1: { label: '待一审', type: 'warning' },
  PENDING_2: { label: '待二审', type: 'warning' },
  PENDING_3: { label: '待三审', type: 'warning' },
  REJECTED: { label: '已退回', type: 'danger' },
  COMPLETED: { label: '已完成', type: 'success' }
}

// 角色映射
export const roleMap = {
  ADMIN: '管理员',
  EDITOR: '作者',
  PENDING_1: '一审员',
  PENDING_2: '二审员'
}

// 获取状态标签类型
export const getStatusType = (status) => {
  return statusMap[status]?.type || 'info'
}

// 获取状态文本
export const getStatusText = (status) => {
  return statusMap[status]?.label || status
}

// 格式化日期
export const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}`
}

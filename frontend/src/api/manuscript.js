import request from '@/utils/request'

// 创建稿件
export const createManuscript = (data) => {
  return request({
    url: '/manuscripts/createManuscripts',
    method: 'post',
    data
  })
}

// 分页查询稿件列表
export const getManuscriptList = (data) => {
  return request({
    url: '/manuscripts/pageList',
    method: 'post',
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    },
    data: {
      title: data.title || '',
      status: data.status || ''
    }
  })
}

// 获取稿件详情
export const getManuscriptDetail = (id) => {
  return request({
    url: '/manuscripts/getDetailById',
    method: 'get',
    params: { id }
  })
}

// 编辑稿件
export const editManuscript = (data) => {
  return request({
    url: '/manuscripts/editManuscripts',
    method: 'put',
    data
  })
}

// 提交一审
export const submitPending1 = (id) => {
  return request({
    url: '/manuscripts/submitPending1',
    method: 'post',
    params: { id }
  })
}

// 审核通过
export const approve = (id) => {
  return request({
    url: '/manuscripts/approve',
    method: 'post',
    params: { id }
  })
}

// 重提
export const resubmit = (id) => {
  return request({
    url: '/manuscripts/resubmit',
    method: 'post',
    params: { id }
  })
}

// 退回
export const reject = (data) => {
  return request({
    url: '/manuscripts/reject',
    method: 'post',
    data
  })
}

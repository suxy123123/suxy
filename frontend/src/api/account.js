import request from '@/utils/request'

// 登录
export const login = (data) => {
  return request({
    url: '/account/login',
    method: 'post',
    data
  })
}

// 注册
export const register = (data) => {
  return request({
    url: '/account/register',
    method: 'post',
    data
  })
}

import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/manuscripts',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'manuscripts',
        name: 'ManuscriptList',
        component: () => import('@/views/ManuscriptList.vue'),
        meta: { title: '稿件列表' }
      },
      {
        path: 'manuscript/create',
        name: 'ManuscriptCreate',
        component: () => import('@/views/ManuscriptEdit.vue'),
        meta: { title: '新建稿件' }
      },
      {
        path: 'manuscript/edit/:id',
        name: 'ManuscriptEdit',
        component: () => import('@/views/ManuscriptEdit.vue'),
        meta: { title: '编辑稿件' }
      },
      {
        path: 'manuscript/detail/:id',
        name: 'ManuscriptDetail',
        component: () => import('@/views/ManuscriptDetail.vue'),
        meta: { title: '稿件详情' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('loginUser')
  
  if (to.meta.requiresAuth && !user) {
    ElMessage.warning('请先登录')
    next('/login')
  } else if (to.path === '/login' && user) {
    next('/')
  } else {
    next()
  }
})

export default router

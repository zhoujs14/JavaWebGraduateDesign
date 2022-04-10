import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component:()=>import("../layout/Layout"),
    redirect:'/user',
    children:[
      {
        path:'user',
        name:'user',
        component:()=>import("../views/User")
      },
      {
        path:'data',
        name:'data',
        component:()=>import("../views/Data")
      }
    ]
  },
  {
    path: '/login',
    name:'login',
    component: ()=>import("../views/Login")
  },
  {
    path: '/register',
    name:'register',
    component: ()=>import("../views/Register")
  },
  {
    path: '/profile',
    name:'profile',
    component:()=>import("../views/Profile")
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

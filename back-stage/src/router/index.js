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
        path:'blog',
        name:'blog',
        component:()=>import("../views/Blog")
      }
    ]
  },
  {
    path: '/login',
    name:'login',
    component: ()=>import("../views/Login")
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

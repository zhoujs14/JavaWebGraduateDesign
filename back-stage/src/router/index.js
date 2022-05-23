import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component:()=>import("../layout/Layout"),
    redirect:'/user',
    children:[
      {
        path:'admin',
        name:'admin',
        component:()=>import("../views/Admin")
      },
      {
        path:'user',
        name:'user',
        component:()=>import("../views/User")
      },
      {
        path:'blog',
        name:'blog',
        component:()=>import("../views/Blog")
      },
      {
        path:'video',
        name:'video',
        component:()=>import("../views/Video")
      },
      {
        path:'category',
        name:'category',
        component:()=>import("../views/Category")
      },
      {
        path:'location',
        name:'location',
        component:()=>import("../views/Location")
      },
      {
        path:'post',
        name:'post',
        component:()=>import("../views/Post")
      },
      {
        path:'comment',
        name:'comment',
        component:()=>import("../views/Comment")
      },
    ]
  },
  {
    path: '/login',
    name:'login',
    component: ()=>import("../views/Login")
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component:()=>import("../layout/Layout"),
    redirect:'/home',
    children:[
      {
        path:'home',
        name:'home',
        component:()=>import("../views/Home")
      },
      {
        path: '/profile',
        name:'profile',
        component:()=>import("../views/Profile")
      },
      {
        path: '/upload',
        name:'upload',
        component:()=>import("../views/Upload")
      },
      {
        path: '/blog',
        name:'blog',
        component:()=>import("../views/Blog")
      },
      {
        path: '/video',
        name:'video',
        component:()=>import("../views/Video")
      },
      {
        path: '/uploadManager',
        name:'uploadManager',
        component:()=>import("../views/UploadManager")
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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

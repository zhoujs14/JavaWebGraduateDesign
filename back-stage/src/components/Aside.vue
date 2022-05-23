<template>
  <!-- calc计算100%视图高度减去Header高度，注意减号的前后要加空格       -->
      <el-menu
          class="el-menu-vertical-demo"
          style="width: 200px;min-height: calc( 100vh - 50px )"
          :default-active=path
          router
      >
<!-- el-menu 加上router属性后 menu-item的index即跳转路由 -->
        <el-sub-menu index="1" v-if="user?.level<3">
          <template #title>账号管理</template>
          <el-menu-item index="admin">管理员管理</el-menu-item>
          <el-menu-item index="user">用户管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>教程管理</template>
          <el-menu-item index="blog">图文教程管理</el-menu-item>
          <el-menu-item index="video">视频教程管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>类别管理</template>
          <el-menu-item index="category">物品种类管理</el-menu-item>
          <el-menu-item index="location">收纳位置管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="post">讨论帖管理</el-menu-item>
        <el-menu-item index="comment">评论管理</el-menu-item>
      </el-menu>
</template>

<script>
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons-vue'
import request from "../../utils/request";

export default {
  name:"Aside",
  components:{
    Location,
    Document,
    IconMenu,
    Setting
  },
  data(){
    return {
      path: this.$route.path.substr(1), //激活当前url对应menu选项
      user:{}
    }
  },
  created() {
    let userStr=sessionStorage.getItem("user") || "{}"
    this.user=JSON.parse(userStr)

    request.get("/admin/"+this.user.id).then(res=>{
      if(res?.code==='0'){
        this.user=res.data
      }
    })
  }
}
</script>
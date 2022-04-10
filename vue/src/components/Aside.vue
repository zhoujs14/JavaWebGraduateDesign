<template>
  <!-- calc计算100%视图高度减去Header高度，注意减号的前后要加空格       -->
      <el-menu
          class="el-menu-vertical-demo"
          style="width: 200px;min-height: calc( 100vh - 50px )"
          :default-active=path
          router
      >
<!-- el-menu 加上router属性后 menu-item的index即跳转路由 -->
        <el-sub-menu index="1">
          <template>系统管理</template>
          <el-menu-item index="user">用户管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="data">数据管理</el-menu-item>
      </el-menu>
</template>

<script>
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons-vue'

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
      path: this.$route.path.substr(1) //激活当前url对应menu选项
    }
  }
}
</script>
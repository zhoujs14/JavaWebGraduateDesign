<template>
  <div class="container">
    <div class="navLeft">后台管理</div>
    <div style="flex:1"></div>
    <div class="navRight">
      <el-dropdown>
        <span class="userDropdown">
          {{nickName}}<arrow-down style="height: 15px;width: 15px;padding-left: 5px"/>
        </span>
        <template #dropdown>
          <el-dropdown-meum v-if="isLogin">
            <el-dropdown-item  @click="()=>this.$router.push('/profile')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="exit">退出登录</el-dropdown-item>
          </el-dropdown-meum>
          <el-dropdown-meum v-if="!isLogin">
            <el-dropdown-item  @click="()=>this.$router.push('/login')">登录</el-dropdown-item>
            <el-dropdown-item @click="()=>this.$router.push('/register')">注册</el-dropdown-item>
          </el-dropdown-meum>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {ArrowDown} from '@element-plus/icons-vue'

export default {
  name: "Header",
  components: {ArrowDown},
  data(){
    return {
      nickName:"",
      isLogin:false
    }
  },
  methods:{
    load(){
      //session中取出user信息
      let user=JSON.parse(sessionStorage.getItem("user"))
      if(!!user&&user?.nickName?.length>0) {
        this.nickName = user.nickName
        this.isLogin=true;
      }
      else {
        this.nickName = '访客'
        this.isLogin=false
      }
    },
    exit(){
      sessionStorage.clear();
      this.load();
    },
    goToProfile(){
      if(this.isLogin){
        this.$router.push('/profile')
      }
    }
  },
  created() {
    this.load();
  }
}
</script>

<style scoped>
.container{
  height: 50px; line-height: 50px;border-bottom: 1px solid #cccccc; display: flex;width: 100%;
}
.navLeft{
  width: 200px;padding-left: 30px;font-weight: bold;color: dodgerblue
}
.navRight{
  padding-right: 30px
}
.userDropdown {
  display: flex;
  align-items: center;
  height: 50px;
}
</style>
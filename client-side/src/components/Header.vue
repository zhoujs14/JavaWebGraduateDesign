<template>
  <header>
    <div class="headerContent">
      <div>
        <span style="margin-right: 20px">家庭物品收纳整理学习网站</span>
        <el-button type="text" @click="()=>this.$router.push('/')">首页</el-button>
      </div>
      <div class="contentRight">
        <div>
          <el-dropdown size="large">
            <el-avatar :size="40" :src="user.avatarSrc" v-if="isLogin" />
            <el-avatar :size="40" v-if="!isLogin">登录</el-avatar>
            <template #dropdown>
              <el-dropdown-menu v-if="isLogin">
                <el-dropdown-item class="item" disabled><div style="display: flex;flex: 1;justify-content: center">{{user?.nickName||""}}</div></el-dropdown-item>
                <el-dropdown-item class="item" @click="goToProfile"><div style="display: flex;flex: 1;justify-content: center">个人中心</div></el-dropdown-item>
                <el-dropdown-item class="item" ><div style="display: flex;flex: 1;justify-content: center">投稿管理</div></el-dropdown-item>
                <el-dropdown-item class="item" @click="exit" divided><div style="display: flex;flex: 1;justify-content: center">退出登录</div></el-dropdown-item>
              </el-dropdown-menu>
              <el-dropdown-menu v-if="!isLogin">
                <el-dropdown-item @click="this.$router.push('/login')">登录</el-dropdown-item>
                <el-dropdown-item @click="this.$router.push('/register')">注册</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="column" style="margin-left: 15px" @click="goToProfile">
          <el-icon :size="20" color="gray"><star/></el-icon>
          <span style="font-size: 13px;color: gray">收藏</span>
        </div>
        <div class="column" style="margin-left: 15px" @click="goToProfile">
          <el-icon :size="20" color="gray"><clock /></el-icon>
          <span style="font-size: 13px;color: gray">历史</span>
        </div>
        <el-button id="contributeBtn" type="primary" size="middle"><el-icon :size="17" style="margin-right: 6px"><upload-filled /></el-icon>投稿</el-button>
      </div>
    </div>
  </header>
</template>

<script>
import {ArrowDown} from '@element-plus/icons-vue'

export default {
  name: "Header",
  components: {ArrowDown},
  data(){
    return {
      nickName:"",
      isLogin:false,
      user:{avatarSrc: "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"}
    }
  },
  methods:{
    load(){
      //session中取出user信息
      let user=JSON.parse(sessionStorage.getItem("user"))
      if(!!user&&user?.nickName?.length>0) {
        this.user=user
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
div {
  font-size: 16px;
}
header {
  width: 100%;
  height: 50px;
  border-bottom: #cccccc 1px solid
}
.headerContent {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  margin: 0 200px;
}
.contentRight {
  display: flex;
  align-items: center
}
#contributeBtn {
  margin-left: 15px;
  width: 75px;
  height: 35px;
}
.item {
  display: flex;
  justify-content: center;
}
</style>
<template>
  <header>
    <div class="headerContent">
      <div>
        <span class="webTitle">家庭物品整理收纳学习网站</span>
        <el-button type="text" @click="()=>this.$router.push('/')">收纳教程</el-button>
        <el-button type="text" @click="()=>this.$router.push('/post')">讨论区</el-button>
      </div>
      <div class="contentRight">
        <div>
        <!-- 头像及头像下拉菜单  -->
          <el-dropdown size="large">
            <el-avatar :size="40" v-if="isLogin" :src="user.avatarSrc" style="cursor: pointer" fit="fill"/>
            <el-avatar :size="40" v-if="!isLogin">登录</el-avatar>
            <template #dropdown>
              <el-dropdown-menu v-if="isLogin">
                <el-dropdown-item class="item" disabled><div class="item">{{user?.nickName||""}}</div></el-dropdown-item>
                <el-dropdown-item class="item" @click="goToProfile"><div class="item">个人中心</div></el-dropdown-item>
                <el-dropdown-item class="item" @click="goToUpload" ><div class="item">投稿管理</div></el-dropdown-item>
                <el-dropdown-item class="item" @click="goToMyPost" ><div class="item">我的讨论</div></el-dropdown-item>
                <el-dropdown-item class="item" @click="exit" divided><div class="item">退出登录</div></el-dropdown-item>
              </el-dropdown-menu>
              <el-dropdown-menu v-if="!isLogin">
                <el-dropdown-item @click="this.$router.push('/login')">登录</el-dropdown-item>
                <el-dropdown-item @click="this.$router.push('/register')">注册</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="row" v-if="isLogin">
          <div class="click" @click="goToFavlist">
            <el-icon :size="20" color="gray"><star/></el-icon>
            <span class="grayText">收藏</span>
          </div>
          <History/>
          <el-button id="contributeBtn" type="primary" @click="()=>this.$router.push('/upload')">
            <el-icon :size="17" style="margin-right: 6px"><upload-filled /></el-icon>
            投稿
          </el-button>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import {ArrowDown} from '@element-plus/icons-vue'
import History from "../components/History";

export default {
  name: "Header",
  components: {History, ArrowDown},
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
    },
    goToUpload(){
      if(this.isLogin){
        this.$router.push('/uploadManager')
      }
    },
    goToMyPost(){
      if(this.isLogin){
        this.$router.push('/myPost')
      }
    },
    goToFavlist(){
      if(this.isLogin){
        this.$router.push('/favlist')
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
.webTitle {
  margin-right: 20px;
  cursor: default;
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
  flex: 1;
}
.grayText {
  font-size: 13px;
  color: gray
}
.click {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  margin-left: 15px;
}
</style>
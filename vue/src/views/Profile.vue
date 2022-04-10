<template>
  <el-container>
    <el-header style="padding: 0">
      <Header/>
    </el-header>
    <el-aside>

    </el-aside>
    <el-main>
      <el-row style="margin-top: 180px;justify-content: center">
        <el-col :span="8" style="background-color: whitesmoke">
          <el-row style="justify-content: center;margin:20px 0">
            <el-avatar :size="80" :src="avatarSrc"/>
          </el-row>
          <el-row style="justify-content: center">
            {{user.nickName||""}}
          </el-row>
          <el-row>
            年龄：{{user.age||"未知"}}
          </el-row>
          <el-row></el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import Header from "../components/Header";
export default {
  name: "profile",
  components: {Header},
  data(){
    return {
      user:{},
      avatarSrc:"http://localhost:9090/files/d3c8254171a943a7b412689d6c729ca1"
    }
  },
  created() {
    let user=JSON.parse(sessionStorage.getItem("user"))
    if(!user){
      this.$router.push("/login")
    }
    else{
      this.user=user
    }
  }
}
</script>
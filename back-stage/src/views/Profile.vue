<template>
  <el-container>
    <el-header style="padding: 0">
      <Header/>
    </el-header>
    <el-aside>

    </el-aside>
    <el-main>
      <el-row class="center">
        <el-col :span="8" style="background-color: whitesmoke;border: black 1px solid;padding: 40px 0">
          <el-row class="center" style="margin:20px 0">
            <el-avatar :size="80" :src="user.avatarSrc"/>
          </el-row>
          <el-row class="center">
            {{user.nickName||""}}
          </el-row>
          <el-row class="center">
            年龄：{{user.age||"未知"}}
          </el-row>
          <el-row class="center">
            <el-upload :show-file-list="false" action="http://localhost:9090/files/upload" :on-success="fileUploadSuccess" :data="user">
              <el-button type="primary">头像上传</el-button>
            </el-upload>
          </el-row>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import Header from "../components/Header";
import request from "../../utils/request";
export default {
  name: "profile",
  components: {Header},
  data(){
    return {
      user:{
        avatarSrc:""
      }
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
  },
  methods:{
    //上传服务器成功
    fileUploadSuccess(res){
      if(res?.data?.length>0) {
        this.user.avatarSrc=res.data
        //保存头像地址
        request.post("/user/avatar",this.user).then(res=>{
          if(res?.code==='0') {
            sessionStorage.setItem("user", JSON.stringify(this.user))
            this.$message({type:"success",message:"头像更新成功"})
          }
          else{
            this.$message({type:"error",message:"头像更新失败:"})
          }
        })
      }
    }
  }
}
</script>

<style>
.center{
  justify-content: center;
  margin-bottom: 12px;
}
</style>
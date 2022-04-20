<template>
  <div style="flex: 1">
    <el-card style="width: 60%;margin: 24px auto 0">
      <template #header>
        <span>我的信息</span>
      </template>
      <!--头像-->
      <div style="display: flex;justify-content: center">
        <el-upload :show-file-list="false" action="http://localhost:9090/files/upload" :on-success="fileUploadSuccess">
          <el-avatar :src="user.avatarSrc" :size="80"></el-avatar>
        </el-upload>
      </div>
      <!--个人信息修改-->
      <div style="margin-top: 24px">
        <el-form :model="user" label-width="120px" >
          <el-form-item label="用户名">
            {{user.username}}
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="user.nickName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="user.age" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="user.gender" label="男">男</el-radio>
            <el-radio v-model="user.gender" label="女">女</el-radio>
            <el-radio v-model="user.gender" label="未知">未知</el-radio>
          </el-form-item>
        </el-form>
        <el-divider/>
        <div style="display: flex;justify-content: center">
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "../../utils/request";
export default {
  name: "profile",
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
    },
    save(){
      request.put("/user",this.user).then(res=> {
        let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

<style>

</style>
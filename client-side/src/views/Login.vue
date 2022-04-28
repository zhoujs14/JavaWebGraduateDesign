<template>
  <div style="width: 100%;height: 100vh;background-color: #f1faee;overflow: hidden">
    <div style="width: 400px;margin:150px auto;text-align: center">
      <div style="color: black;font-size: 30px;padding-bottom: 20px">欢迎登陆</div>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="AvatarIcon" v-model="form.username" label="用户名" ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="KeyIcon" v-model="form.password" label="密码" show-password @keyup.enter.native="submit"/>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="submit">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="info" @click="this.$router.push('/register')"> 注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {Avatar,Key} from "@element-plus/icons-vue";
import request from "../../utils/request";

export default {
  name: "Login",
  data(){
    return {
      form:{},
      AvatarIcon:Avatar,
      KeyIcon:Key,
      rules:{
        username:[{required:true,message:"请输入用户名",trigger:'blur'}],
        password:[{required:true,message:"请输入密码",trigger:'blur'}]
      }
    }
  },
  methods:{
    submit(){
      this.$refs["form"].validate(valid=>{
        if(valid){
          request.post("/user/login",this.form).then(res=>{
            if(res?.code==='0'){
              //登录成功
              this.$message({type:"success",message:"登录成功"})
              sessionStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
              this.$router.push("/"); //登录成功跳转主页
            }else{
              //登录失败
              this.$message({type:"error",message:`登录失败，${res?.msg}`})
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
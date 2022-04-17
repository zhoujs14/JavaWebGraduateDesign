<template>
  <div style="width: 100%;height: 100vh;background-color: #f1faee;overflow: hidden">
    <div style="width: 400px;margin:150px auto;text-align: center">
      <div style="color: black;font-size: 30px;padding-bottom: 20px">注册新账号</div>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="username">
          <el-input  v-model="form.username" placeholder="用户名" clearable></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input  v-model="form.password" placeholder="密码" show-password/>
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input  v-model="form.confirm" placeholder="确认密码" show-password/>
        </el-form-item>
        <el-form-item prop="nickName">
          <el-input  v-model="form.nickName" placeholder="昵称" clearable/>
        </el-form-item>
        <el-form-item prop="age">
          <el-input  v-model.number="form.age" type="text" placeholder="年龄" min="0"/>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio v-model="form.gender" label="男">男</el-radio>
          <el-radio v-model="form.gender" label="女">女</el-radio>
          <el-radio v-model="form.gender" label="未知">未知</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";

const userNameMinLen=2;
const userNameMaxLen=18;
const passwordMinLen=6;
const passwordMaxLen=20;
const nickNameMinLen=2;
const nickNameMaxLen=12;
const maxAge=200;

export default {
  name: "Register",
  data(){
    return {
      form:{
        username:"",
        password:"",
        confirm:"",
        nickName:"",
        age:"",
        gender:"未知"
      },
      rules:{
        username: [{validator:this.checkUserName,trigger:'blur'}],
        password: [{validator:this.checkPwd,trigger:'blur'}],
        confirm: [{validator:this.checkConfirm,trigger:'blur'}],
        nickName: [{validator:this.checkNickName,trigger:'blur'}],
        age:[{validator:this.checkAge,trigger:"blur"}]
      }
    }
  },
  methods:{
    //表单校验规则
    checkUserName(rule,value,callback){
      if(value===''||value.length<userNameMinLen) callback(new Error(`用户名长度不能小于${userNameMinLen}个字符`));
      else if (value.length>userNameMaxLen) callback(new Error(`用户名长度不得超过个${userNameMaxLen}个字符`));
      callback()
    },
    checkPwd(rule,value,callback){
      if(value===''||value.length<passwordMinLen) callback(new Error(`密码长度不能小于${userNameMinLen}个字符`));
      else if (value.length>passwordMaxLen) callback(new Error(`密码长度不得超过个${userNameMaxLen}个字符`));
      callback()
    },
    checkConfirm(rule,value,callback){
      if(value===''||value.length<passwordMinLen) callback(new Error(`密码长度不能小于${userNameMinLen}个字符`));
      else if (value.length>passwordMaxLen) callback(new Error(`密码长度不得超过个${userNameMaxLen}个字符`));
      else if(value!==this.form.password)callback(new Error(`两次输入密码不一致`));
      callback()
    },
    checkNickName(rule,value,callback){
      if(value===''||value.length<nickNameMinLen) callback(new Error(`昵称长度不能小于${userNameMinLen}个字符`));
      else if (value.length>nickNameMaxLen) callback(new Error(`昵称长度不得超过个${userNameMaxLen}个字符`));
      callback()
    },
    checkAge(rule,value,callback){
      if(value==='') callback(new Error(`请输入年龄`));
      else if (isNaN(value)||value<1) callback(new Error("请输入正整数"))
      else if(value>maxAge) callback(new Error("超出最大范围"))
      callback()
    },
    //注册
    register(){
      this.$refs["form"].validate(valid=>{
        if(valid){
          request.post("/user/register",this.form).then(res=>{
            console.log("res")
            if(res?.code==='0'){
              //注册成功
              this.$message({type:"success",message:"注册成功"})
              this.$router.push("/login"); //登录成功跳转登录页面
            }else{
              //注册失败
              this.$message({type:"error",message:`注册失败，${res?.msg}`})
            }
          })
        }
      })
    }
  }
}
</script>
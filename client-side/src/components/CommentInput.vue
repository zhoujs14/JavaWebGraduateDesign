<template>
  <el-row style="margin-bottom: 14px;margin-top: 4px">
    <el-col :span="2" class="center">
      <el-avatar :src="avatar"/>
    </el-col>
    <el-col :span="20">
      <el-input type="textarea"
                :placeholder="placeholder||'发布一条友善的评论'"
                v-model="content"
                :autosize="{ minRows: 2, maxRows: 6 }">
      </el-input>
    </el-col>
    <el-col :span="2" class="center">
      <el-button @click="submit" type="primary" :disabled="btnDisabled">评论</el-button>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CommentInput",
  props:['placeholder'],
  data(){
    return {
      content:'',
      avatar:'',
      btnDisabled:false
    }
  },
  methods:{
    submit(){
      this.$emit('onSubmit',this.content)
      this.content=''
    }
  },
  created() {
    let user= JSON.parse(sessionStorage.getItem('user'))
    if(user!==null) this.avatar=user.avatarSrc
    else this.btnDisabled=true
  }
}
</script>

<style scoped>

</style>
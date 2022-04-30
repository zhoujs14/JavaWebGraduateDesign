<template>
  <el-row style="margin-bottom: 14px">
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
      <el-button @click="submit" type="primary">评论</el-button>
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
      avatar:''
    }
  },
  methods:{
    submit(){
      this.$emit('onSubmit',this.content)
    }
  },
  created() {
    let user= JSON.parse(sessionStorage.getItem('user'))
    this.avatar=user.avatarSrc
  }
}
</script>

<style scoped>

</style>
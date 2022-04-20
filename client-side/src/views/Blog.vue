<template>
    <el-row style="padding: 30px 0">
      <!-- 作者信息卡片  -->
      <el-col :span="4">
        <el-card style="margin-right: 8px;display: flex;flex-direction: column;align-items: center">
          <div style="display: flex;justify-content: center;margin-bottom: 8px"><el-avatar :src="author.avatarSrc"/></div>
          <div>{{author.nickName}}</div>
        </el-card>
      </el-col>
    <!--  正文容器    -->
      <el-col :span="16">
        <el-card>
          <h1 >{{currentBlog.title}}</h1>
          <div class="time">{{currentBlog.time}}</div>
          <el-divider/>
          <div v-html="currentBlog.content"></div>
        </el-card>
        <el-card style="margin-top: 20px">
          评论
        </el-card>
      </el-col>
      <el-col :span="4">

      </el-col>
    </el-row>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "Blog",
  data(){
    return {
      currentBlog: {
        title:"",
        content:"default"
      },
      author:{}
    }
  },
  methods:{
    queryAuthor(){
      request.get('/user/'+this.currentBlog.authorId).then(res=>{
        if(res?.code==='0'){
          this.author=res.data
        }
      })
    }
  },
  created() {
    let bid=window.location.search.replace("?bid=","")
    console.log(bid)
    if(bid){
      request.get("/blog/"+bid).then(res=>{
        if(res?.code==='0'){
          this.currentBlog=res.data
          this.queryAuthor(this.currentBlog.authorId);
        }
      })
    }
  }
}
</script>

<style scoped>
.time {
  margin-top: 6px;
  font-size: 14px;
  color: dimgray
}
</style>
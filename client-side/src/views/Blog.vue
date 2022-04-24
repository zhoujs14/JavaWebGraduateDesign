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
    <!--  标题        -->
          <h1 >{{currentBlog.title}}</h1>
          <el-row class="subTitle">
            <div class="time">{{currentBlog.time}}</div>
            <el-tag type="success" v-if="currentBlog.cateName" style="margin-right: 8px">{{currentBlog.cateName}}</el-tag>
            <el-tag type="warning" v-if="currentBlog.locationName" style="margin-right: 8px">{{currentBlog.locationName}}</el-tag>
          </el-row>
          <el-divider/>
    <!--   正文       -->
          <div v-html="currentBlog.content"></div>
        </el-card>
        <el-card style="margin-top: 20px" body-style="padding:20px 3px">
          <Comment type="blog" :id="currentBlog.id" v-if="currentBlog.id"/>
        </el-card>
      </el-col>
      <el-col :span="4">

      </el-col>
    </el-row>
</template>

<script>
import request from "../../utils/request";
import Comment from "../components/Comment";

export default {
  name: "Blog",
  components: {Comment},
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
  font-size: 14px;
  color: dimgray;
  margin-right: 8px;
}

.subTitle {
  align-items: center;
  margin-top: 6px
}
</style>
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
        <h1>{{currentVideo.title}}</h1>
        <div class="time">{{currentVideo.time}}</div>
        <div id="mse"></div>
      </el-card>
      <el-card style="margin-top: 20px">
        <Comment type="video" :id="currentVideo.id" v-if="currentVideo.id"/>
      </el-card>
    </el-col>
    <el-col :span="4">

    </el-col>
  </el-row>
</template>

<script>
import Comment from "../components/Comment";
import Player from 'xgplayer'
import request from "../../utils/request";

let player
let vid

export default {
  name: "Video",
  components:{
    Comment
  },
  data(){
    return {
      author:{
        avatarSrc:'',
        nickName:'视频作者'
      },
      currentVideo:{
        title:"视频名称",
        time:"yyyy-mm-dd"
      }
    }
  },
  methods:{
    queryAuthor(){
      request.get('/user/'+this.currentVideo.authorId).then(res=>{
        if(res?.code==='0'){
          this.author=res.data
        }
      })
    }
  },
  created() {
    let vid=window.location.search.replace("?vid=","")
    if(vid){
      request.get("/video/"+vid).then(res=>{
        if(res?.code==='0'){
          this.currentVideo=res.data
          this.queryAuthor(this.currentVideo.authorId);
          player = new Player({
            id: 'mse',
            url: this.currentVideo.src,
            fluid: true, //流式布局
            poster: this.currentVideo.cover //封面
          });
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
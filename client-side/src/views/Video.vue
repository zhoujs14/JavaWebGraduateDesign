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
      <!-- 点赞 收藏  -->
      <el-card style="margin-top: 20px" body-style="padding: 8px 16px">
        <div class="row" style="justify-content: space-between">
          <div class="row">
            <div class="iconWrapper" @click="like" v-if="isLike!=null">
              <img v-if="isLike" :src="require('@/assets/img/liked.png')" class="likeIcon"/>
              <img v-else :src="require('@/assets/img/like.png')" class="likeIcon"/>
              {{currentVideo.likesCount}}
            </div>
            <div class="iconWrapper" @click="star">
              <el-icon v-if="isStar" color="#409EFF" :size="35" style="margin-right: 7px"><star-filled /></el-icon>
              <el-icon v-else color="darkgray" :size="35" style="margin-right: 7px"><star-filled /></el-icon>
              {{currentVideo.star}}
            </div>
          </div>
          <a class="iconWrapper" :href="downloadUrl" style="margin-right: 16px">
            <el-icon :size="25" :color="gray"><download /></el-icon>
          </a>
        </div>
      </el-card>
      <el-card style="margin-top: 20px">
        <CommentList type="video" :id="currentVideo.id" v-if="currentVideo.id"/>
      </el-card>
    </el-col>
    <el-col :span="4">

    </el-col>
  </el-row>
</template>

<script>
import CommentList from "../components/CommentList";
import Player from 'xgplayer'
import request from "../../utils/request";

let player
let vid

export default {
  name: "Video",
  components:{
    CommentList
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
      },
      isStar:false,
      isLike:null,
      downloadUrl:''
    }
  },
  methods:{
    queryAuthor(){
      request.get('/user/'+this.currentVideo.authorId).then(res=>{
        if(res?.code==='0'){
          this.author=res.data
        }
      })
    },
    //查询是否点赞
    queryLike(){
      request.get('/likes',{
        params:{
          type:'video',
          cid: this.currentVideo.id
        }
      }).then(res=>{
        if(res?.code==='0'){
          this.isLike=res.data===1
        }
        else {
          this.$message({type:'error',message:'获取是否点赞失败;'+res?.msg})
        }
      })
    },
    //查询是否收藏
    queryStar(){
      request.get('/star',{
        params:{
          type:'video',
          cid: this.currentVideo.id
        }
      }).then(res=>{
        if(res?.code==='0'){
          this.isStar=res.data===1
        }
        else {
          this.$message({type:'error',message:'获取是否收藏失败;'+res?.msg})
        }
      })
    },
    like(){
      request.post('/likes',{
        type:'video',
        contentId:this.currentVideo.id
      }).then(res=>{
        if(res?.code==='0'){
          if(res.data===1) {
            this.$message({type: 'success', message: '点赞成功,你的认可是作者最大的动力'})
            this.isLike=true
            this.currentVideo.likesCount+=1
          }
          else {
            this.$message({type: 'success', message: '已取消点赞'})
            this.isLike=false
            this.currentVideo.likesCount-=1
          }
        }else{
          this.$message({type:'error',message:"操作失败;"+res?.msg})
        }
      })
    },
    star(){
      request.post('/star',{
        type:'video',
        contentId:this.currentVideo.id
      }).then(res=>{
        if(res?.code==='0'){
          if(res.data===1) {
            this.$message({type: 'success', message: '收藏成功'})
            this.isStar=true
            this.currentVideo.star+=1
          }
          else {
            this.$message({type: 'success', message: '已取消收藏'})
            this.isStar=false
            this.currentVideo.star-=1
          }
        }else{
          this.$message({type:'error',message:"操作失败;"+res?.msg})
        }
      })
    },
    saveHistory(){
      request.post('/watch',{
        type:'video',
        contentId:this.currentVideo.id
      })
    }
  },
  created() {
    let vid=window.location.search.replace("?vid=","")
    if(vid){
      this.currentVideo.id=vid
      this.queryLike()
      this.queryStar()
      this.saveHistory()
      request.get("/video/"+vid).then(res=>{
        if(res?.code==='0'){
          this.currentVideo=res.data
          this.queryAuthor()
          this.downloadUrl='http://localhost:9090/files/downloadVideo/'+this.currentVideo.src.slice(34)
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
.iconWrapper {
  margin-right: 45px;
  display: flex;
  flex-direction: row;
  align-items: center;
  cursor: pointer;
}

.likeIcon {
  width: 25px;
  height:24px;
  margin-right: 8px
}
</style>
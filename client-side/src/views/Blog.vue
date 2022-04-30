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
          <div v-html="currentBlog.content" class="content"></div>
        </el-card>
        <!-- 点赞 收藏  -->
        <el-card style="margin-top: 20px" body-style="padding: 8px 16px">
          <div class="row">
            <div class="iconWrapper" @click="like" v-if="isLike!=null">
              <img v-if="isLike" :src="require('@/assets/img/liked.png')" class="likeIcon"/>
              <img v-else :src="require('@/assets/img/like.png')" class="likeIcon"/>
              {{currentBlog.likesCount}}
            </div>
            <div class="iconWrapper" @click="star">
              <el-icon v-if="isStar" color="#409EFF" :size="35" style="margin-right: 7px"><star-filled /></el-icon>
              <el-icon v-else color="darkgray" :size="35" style="margin-right: 7px"><star-filled /></el-icon>
              {{currentBlog.star}}
            </div>
          </div>
        </el-card>
        <!--  评论   -->
        <el-card style="margin-top: 20px" body-style="padding:20px 3px">
          <CommentList type="blog" :id="currentBlog.id" v-if="currentBlog.id"/>
        </el-card>
      </el-col>
      <el-col :span="4">
      </el-col>
    </el-row>
</template>

<script>
import request from "../../utils/request";
import CommentList from "../components/CommentList";

export default {
  name: "Blog",
  components: {CommentList},
  data(){
    return {
      currentBlog: {
        title:"",
        content:"default"
      },
      author:{},
      isStar:false,
      isLike:null
    }
  },
  methods:{
    //查询作者信息
    queryAuthor(){
      request.get('/user/'+this.currentBlog.authorId).then(res=>{
        if(res?.code==='0'){
          this.author=res.data
        }
      })
    },
    //查询是否点赞
    queryLike(){
      request.get('/likes',{
        params:{
          type:'blog',
          cid: this.currentBlog.id
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
          type:'blog',
          cid: this.currentBlog.id
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
    //保存浏览记录
    saveHistory(){
      request.post('/watch',{
        type:'blog',
        contentId:this.currentBlog.id
      })
    },
    like(){
      request.post('/likes',{
        type:'blog',
        contentId:this.currentBlog.id
      }).then(res=>{
        if(res?.code==='0'){
          if(res.data===1) {
            this.$message({type: 'success', message: '点赞成功,你的认可是作者最大的动力'})
            this.isLike=true
            this.currentBlog.likesCount+=1
          }
          else {
            this.$message({type: 'success', message: '已取消点赞'})
            this.isLike=false
            this.currentBlog.likesCount-=1
          }
        }else{
          this.$message({type:'error',message:"操作失败;"+res?.msg})
        }
      })
    },
    star(){
      request.post('/star',{
        type:'blog',
        contentId:this.currentBlog.id
      }).then(res=>{
        if(res?.code==='0'){
          if(res.data===1) {
            this.$message({type: 'success', message: '收藏成功'})
            this.isStar=true
            this.currentBlog.star+=1
          }
          else {
            this.$message({type: 'success', message: '已取消收藏'})
            this.isStar=false
            this.currentBlog.star-=1
          }
        }else{
          this.$message({type:'error',message:"操作失败;"+res?.msg})
        }
      })
    },
  },
  created() {
    let bid=window.location.search.replace("?bid=","")
    if(bid){
      this.currentBlog.id=bid
      this.queryLike();
      this.queryStar();
      this.saveHistory();
      request.get("/blog/"+bid).then(res=>{
        if(res?.code==='0'){
          this.currentBlog=res.data
          this.queryAuthor();
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
<template>
  <el-row style="padding: 30px 0">
    <el-col :span="2"/>
    <el-col :span="20">
      <h4 style="margin-bottom: 8px">{{currentPost.title}}</h4>
      <PostCard :post="this.currentPost" @onComment="onComment"/>
      <el-card style="margin-top: 12px">
        <PostForm :parent-id="currentPost.id" @onSubmit="window.location.reload()" :focus="focus"/>
      </el-card>
    </el-col>
    <el-col :span="4">
    </el-col>
  </el-row>
</template>

<script>
import request from "../../utils/request";
import Comment from "../components/CommentList";
import PostForm from "../components/PostForm";
import PostCard from "../components/PostCard";

export default {
  name: "PostDetail",
  components: {PostCard, PostForm, Comment},
  data(){
    return {
      currentPost: {
        title:"",
        content:"default"
      },
      author:{},
      isStar:false,
      isLike:null,
      focus:0
    }
  },
  methods:{
    onComment(post){
      if(!post.parentId){
        //回复根节点,滑动到底部并聚焦编辑器
        console.log('focus')
        this.focus+=1;
        let height = document.body.clientHeight;
        window.scroll({ top: height , left: 0, behavior: 'smooth' });
      }
      //回复子节点,评论
      else {

      }
    }
  },
  created() {
    let pid=window.location.search.replace("?pid=","")
    if(pid){
      this.currentPost.id=pid
      request.get("/post/"+pid).then(res=>{
        if(res?.code==='0'){
          this.currentPost=res.data
        }
        else this.$message({type:'error',message:'获取帖子失败;'+res?.msg})
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
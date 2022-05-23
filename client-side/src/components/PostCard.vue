<template>
  <el-row style="border: lightgray 1px solid">
    <!--  左侧作者信息  -->
    <el-col :span="4" style="background-color: ghostwhite">
      <div class="column" style="padding-top: 20px">
        <el-avatar :src="post.authorAvatar" :size="55" shape="square" class="avatar"/>
        <div style="font-size: 14px;margin-top: 16px">{{this.post.authorName}}</div>
      </div>
    </el-col>
    <!-- 正文  -->
    <el-col :span="20">
      <div class="contentWrapper">
        <div v-html="post.content" class="content"></div>
        <div class="footer">
          {{post.time}}
          <el-button type="text" @click="onComment(this.post)" style="margin-left: 4px;font-size: 14px">{{visibleText}}</el-button>
        </div>
      </div>
      <!--  非根节点显示评论    -->
      <div v-if="post.parentId" style="background-color: ghostwhite;margin: 4px 16px 12px 16px;padding:8px 4px 1px 4px;">
        <CommentList type="post" :id="post.id" input-position="bottom" @showInput="showInput"/>
        <CommentInput v-if="inputVisible" @onSubmit="save" :placeholder="placeholder"/>
      </div>
    </el-col>
  </el-row>
  <div v-if="!post.parentId" v-for="item in post.children">
    <PostCard :post="item" @onComment="onComment(item)"/>
  </div>
</template>

<script>
import CommentList from "./CommentList";
import CommentInput from "./CommentInput";
import request from "../../utils/request";
export default {
  name: "PostCard",
  components: {CommentInput, CommentList},
  props:['post'],
  data(){
    return {
      visibleText:'回复',
      comment:{
        type:'post',
        content:"",
        rootId: this.post.id,
        parentId:null
      },
      inputParent:null,
      placeholder:null,
      inputVisible:false
    }
  },
  methods:{
    onComment(post){
      if(!post?.parentId) this.$emit('onComment',post)
      else {
        this.showInput(null)
      }
    },
    save(content) {
      this.comment.content=content
      request.post("/comment", this.comment).then(res => {
        if (res?.code === '0') {
          this.$message({type: 'success', message: '评论成功'})
          this.comment.content=''
          window.location.reload()
        } else {
          this.$message({type: 'error', message: '评论失败,' + res?.msg})
        }
      }).finally(
          ()=>{
            this.load()
          }
      )
    },
    showInput(parent){
      if(this.inputParent===parent){
        this.inputVisible=!this.inputVisible
        this.visibleText=this.inputVisible?'收起回复':'回复'
      }
      else {
        this.placeholder=parent?.parentId!==null?"回复:@"+parent?.authorName:null
        this.inputVisible=!!parent?true:!this.inputVisible
        this.inputParent=parent
        this.visibleText=this.inputVisible?'收起回复':'回复'
      }
    }
  }
}
</script>

<style scoped>
.avatar {
  padding: 2px;
  border: gray 1px solid;
  background-color: white
}
.contentWrapper {
  display: flex;
  flex-direction: column;
  padding: 16px;
  align-items: flex-start;
  justify-content:space-between;
  min-height: 300px
}
.footer{
  display: flex;
  flex-direction: row;
  align-self: flex-end;
  font-size: 12px;
  color: gray;
  margin-bottom: -12px;
  margin-right: -12px;
  align-items: center;
}

.content {
  WORD-WRAP:break-word;
  TABLE-LAYOUT:fixed;
  word-break:break-all;
}

</style>
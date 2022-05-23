<template>
<!--  根级评论-->
  <div v-if="this.layer===0">
    <el-row style="margin-top: 8px;min-height: 80px">
       <!--  头像    -->
      <el-col :span="2" class="center">
        <el-avatar :src="comment.avatarSrc" :size="40"/>
      </el-col>
      <!-- 评论内容   -->
      <el-col :span="20" class="comment">
        <div style="font-size: 14px;color: palevioletred">{{comment.authorName}}</div>
        <div style="font-size: 16px">{{comment.content}}</div>
        <div class="row">
          <div style="font-size: 10px;color: gray">{{comment.time}}</div>
          <div class="re" @click="showInput(comment)">回复</div>
        </div>
      </el-col>
      <el-col :span="2" class="center">
        <el-dropdown v-if="this.comment.authorId===this.user?.id">
          <el-icon><more /></el-icon>
          <template #dropdown>
            <el-button style="padding: 8px 14px;font-size: 14px" type="text" @click="deleteComment">删除</el-button>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>
<!--    子评论-->
    <el-row v-if="comment.children.length>0">
      <el-col :span="2"></el-col>
      <el-col :span="20">
        <div v-for="subComment in comment.children">
          <Comment :comment="subComment" :layer="1" :parent="comment" @showInput="showInput"/>
        </div>
      </el-col>
    </el-row>
<!--    回复输入框-->
    <el-row v-if="inputVisible">
      <el-col :span="2"/>
      <el-col :span="22">
        <CommentInput  style="margin-top: 12px" @onSubmit="submit" :placeholder="placeholder"/>
      </el-col>
    </el-row>
  </div>

<!--  子级评论-->
  <div v-else>
    <div class="row" style="margin-top: 4px;justify-content: space-between">
    <!-- 内容 -->
      <div class="row">
        <el-avatar :src="comment.avatarSrc" :size="25" style="margin-right: 12px"/>
        <div style="font-size: 13px;color: palevioletred;margin-right: 4px">{{comment.authorName}}</div>
        <div style="font-size: 14px">
          <span v-if="this.layer>1" style="color: dodgerblue">回复:@{{this.parent.authorName}}</span>
          {{comment.content}}
        </div>
      </div>
      <el-dropdown v-if="this.comment.authorId===this.user?.id">
        <el-icon><more /></el-icon>
        <template #dropdown>
          <el-button style="padding: 8px 14px;font-size: 14px" type="text" @click="deleteComment">删除</el-button>
        </template>
      </el-dropdown>
    </div>
    <div class="row" style="margin-top: 6px;margin-left: 37px">
      <div style="font-size: 10px;color: gray">{{comment.time}}</div>
      <div class="re" @click="showParentInput">回复</div>
    </div>
    <!-- 子评论 -->
    <el-row v-if="comment.children.length>0">
      <el-col :span="24">
        <div v-for="subComment in comment.children">
          <el-divider style="margin: 4px 0"/>
          <Comment :comment="subComment" :layer="this.layer+1" :parent="comment" @showInput="showInput"/>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CommentInput from "./CommentInput";
import request from "../../utils/request";
export default {
  name: "Comment",
  components: {CommentInput},
  props:['comment','layer',"parent"],
  data(){
    return{
      inputVisible:false,
      inputParent:{},
      placeholder: null,
      user:{}
    }
  },
  methods:{
    showInput(parent){
      //当前为底层comment
      if(this.layer===0){
        if(this.inputParent===parent){
          this.inputVisible=!this.inputVisible
        }
        else {
          this.placeholder=parent.parentId===null?null:"回复:@"+parent.authorName
          this.inputVisible=true
          this.inputParent=parent
        }
      }
      else {
        this.$emit('showInput',parent)
      }
    },
    //子集评论点击回复后发送给上一级评论
    showParentInput(){
      this.$emit('showInput',this.comment)
    },
    submit(content){
      request.post('/comment',{
        content:content,
        parentId:this.inputParent.id,
        rootId:this.inputParent.rootId,
        type:this.inputParent.type
      }).then(res=>{
        if(res?.code==='0'){
          this.$message({type:'success',message:'评论成功'})
          this.$emit("onSubmit")
        }else {
          this.$message({type:'error',message:'评论失败;'+res?.msg})
        }
      })
    },
    deleteComment(){
      request.delete("/comment/"+this.comment.id).then(res=>{
        if(res?.code==='0'){
          this.$message({type:'success',message:'删除评论成'});
          window.location.reload()
        }
        else this.$message({type:'error',message:'删除评论失败;'+res?.msg})
      })
    }
  },
  created() {
    let user=sessionStorage.getItem("user")
    this.user=JSON.parse(user)
  }
}
</script>

<style scoped>
.comment{
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}
.re {
  font-size: 10px;
  margin-left: 12px;
  cursor: pointer;
  color: gray;
}
.re:hover{
  color: dodgerblue
}
</style>
<template>
  <div v-if="type!=='post'">
    <CommentInput @onSubmit="save"/>
    <div>
      <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <li v-for="c in records" :key="c" class="infinite-list-item" :infinite-scroll-disabled="isOver">
          <el-divider style="margin: 8px 0"/>
          <Comment :comment="c" :layer="0" @onSubmit="onSubmit"/>
        </li>
      </ul>
    </div>
  </div>
  <div v-else>
    <div>
      <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <li v-for="c in records" :key="c" class="infinite-list-item" :infinite-scroll-disabled="isOver">
          <Comment :comment="c" :layer="1" @showInput="showInput"/>
          <el-divider style="margin: 4px 0"/>
        </li>
      </ul>
    </div>
    <CommentInput v-if="inputVisible" @onSubmit="save"/>
  </div>
</template>

<script>
import request from "../../utils/request";
import Comment from "./Comment";
import CommentInput from "./CommentInput";

let editorConfig= {}
let editor

export default {
  name: "CommentList",
  components: {CommentInput, Comment},
  props:['type','id'],
  data(){
    return {
      currentPage:1,
      pageSize: 10,
      records:[],
      isOver:false,
      inquiring:false,
      comment:{
        type:this.type,
        content:"",
        rootId: this.id
      },
      inputVisible:false
    }
  },
  methods: {
    load(){
      //若正在查询分页数据，1秒后再查询第二次
      if(this.inquiring) {
        setTimeout(()=>{
          this.load(),1000
        })
      }
      //当前未在查询分页数据
      else {
        //数据已全部读取
        if(this.isOver) {
          return
        }
        //数据未读取完毕
        else{
          this.inquiring=true
          request.get('/comment',{
            params:{
              pageNum:this.currentPage,
              pageSize:this.pageSize,
              type:this.type,
              rootId:this.id
            }
          }).then(res=>{
            if(res?.code==='0'){
              this.records=this.records.concat(res.data.records)
              if(res?.data?.total<=this.pageSize*this.currentPage) this.isOver=true //已加载全部内容
              this.currentPage+=1
            }
            else{
              this.$message({type:'error',message:'加载出错,错误信息:'+res?.msg})
            }
          }).finally(
              ()=> {
                this.inquiring = false
              }
          )
        }
      }
    },
    showInput(parent){
      this.$emit('showInput',parent)
    },
    save(content) {
      this.comment.content=content
      request.post("/comment", this.comment).then(res => {
        if (res?.code === '0') {
          this.$message({type: 'success', message: '评论成功'})
          this.onSubmit()
        } else {
          this.$message({type: 'error', message: '评论失败,' + res?.msg})
        }
      }).finally(
          ()=>{
            this.load()
          }
      )
    },
    onSubmit(){
      this.initialize()
      this.load()
    },
    initialize(){
      this.currentPage=1,
      this.pageSize= 10,
      this.records=[],
      this.isOver=false,
      this.comment={
          type:this.type,
          content:"",
          rootId: this.id
      }
    }
  },
  created() {
    this.load()
  }
}
</script>

<style scoped>

</style>
<template>
  <div>
    <el-row>
      <el-col :span="2" class="center">
        <el-avatar :src="this.avatarSrc"/>
      </el-col>
      <el-col :span="20">
        <el-input type="textarea"
                  placeholder="发布一条友善的评论"
                  v-model="this.comment.content"
                  :autosize="{ minRows: 2, maxRows: 6 }">
        </el-input>
      </el-col>
      <el-col :span="2" class="center">
        <el-button @click="save" type="primary" size="large">评论</el-button>
      </el-col>
    </el-row>
    <div>
      <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
        <li v-for="c in records" :key="c" class="infinite-list-item" :infinite-scroll-disabled="isOver">
          <el-divider/>
          <el-row style="margin-top: 8px;min-height: 80px">
            <el-col :span="2" class="center">
              <el-avatar :src="c.avatarSrc"/>
            </el-col>
            <el-col :span="20" class="comment">
              <div style="font-size: 14px;color: palevioletred">{{c.authorName}}</div>
              <div style="font-size: 16px">{{c.content}}</div>
              <div style="font-size: 10px;color: gray">{{c.time}}</div>
            </el-col>
            <el-col :span="2" class="center">
            </el-col>
          </el-row>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";

let editorConfig= {}
let editor

export default {
  name: "Comment",
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
        parentId: this.id
      },
      avatarSrc:''
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
              parentId:this.id
            }
          }).then(res=>{
            if(res?.code==='0'){
              this.records=this.records.concat(res.data.records)
              console.log('records:',this.records)
              if(res?.data?.total<=this.pageSize*this.currentPage) this.isOver=true //已加载全部内容
              this.currentPage+=1
            }
            else{
              this.$message({type:'error',message:'加载出错,错误信息:'+res?.msg})
            }
          }).finally(
              ()=>this.inquiring=false
          )
        }
      }

    },
    save() {
      request.post("/comment", this.comment).then(res => {
        if (res?.code === '0') {
          this.$message({type: 'success', message: '评论成功'})
          this.comment.content=''
        } else {
          this.$message({type: 'error', message: '评论失败,' + res?.msg})
        }
      }).finally(
          ()=>{
            this.load()
          }
      )
    }
  },
  created() {
    let user= JSON.parse(sessionStorage.getItem('user'))
    this.avatarSrc=user.avatarSrc
    this.load()
  }
}
</script>

<style scoped>
.comment{
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}
</style>
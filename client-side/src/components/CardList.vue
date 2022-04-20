<template>
  <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
    <li v-for="i in records" :key="i" class="infinite-list-item" :infinite-scroll-disabled="isOver">
      <Card :data="i" :type="this.type"/>
    </li>
  </ul>
</template>

<script>
import Card from "./Card";
import request from "../../utils/request";

export default {
  name: "CardList",
  components:{
    Card
  },
  props:["type"],
  data(){
    return {
      currentPage:1,
      pageSize: 10,
      records:[],
      isOver:false,
      inquiring:false,
      queryUrl:""
    }
  },
  methods:{
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
          let url=this.type==='blog'?"/blog":'/video'
          request.get(url,{
            params:{
              pageNum:this.currentPage,
              pageSize:this.pageSize,
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
              ()=>this.inquiring=false
          )
        }
      }

    }
  },
  mounted() {
    this.currentPage=1
    this.pageSize=10
    this.load()
  }
}
</script>

<style scoped>

</style>
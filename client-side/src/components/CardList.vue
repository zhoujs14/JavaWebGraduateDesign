<template>
  <ul v-infinite-scroll="load" style="overflow: auto" v-if="records.length>0">
    <li v-for="i in records" :key="i" :infinite-scroll-disabled="isOver" >
      <Card :data="i" :type="this.type" :edit="this.editable"/>
    </li>
  </ul>
  <div v-else style="color: gray;text-align: center">暂无数据</div>
</template>

<script>
import Card from "./Card";
import request from "../../utils/request";

export default {
  name: "CardList",
  components:{
    Card
  },
  props:["type","searchPattern","keyWords","categoryId","locationId","editable"],
  data(){
    return {
      currentPage:1,
      pageSize: 10,
      records:[],
      isOver:false,
      inquiring:false,
    }
  },
  //监听搜素参数变化,重新请求数据
  watch:{
    searchPattern:{
      handler(){
        this.initialize()
        this.load()
      }
    },
    keyWords:{
      handler(){
        this.initialize()
        this.load()
      }
    },
    categoryId:{
      handler(){
        this.initialize()
        this.load()
      }
    },
    locationId:{
      handler(){
        this.initialize()
        this.load()
      }
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
              searchPattern:this.searchPattern,
              keyWords:this.keyWords,
              categoryId:this.categoryId,
              locationId:this.locationId
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
    },
    initialize(){
      this.currentPage=1
      this.pageSize=10
      this.records=[]
      this.isOver=false
    },
  },
  created() {
    this.initialize()
    this.load()
  },
}
</script>

<style scoped>

</style>
<template>
<div v-if="this.records.length>0">
  <div class="row">
    <div style="width: 208px;margin: 16px 8px;cursor: pointer" class="column" v-for="item in records" @click="goToDetail(item)">
      <el-image :src="item.cover" style="width: 208px;height: 117px"/>
      <div style="margin-top: 4px;width: 100%;overflow: hidden">
        <div style="font-size: 14px;line-height: 14px;height: 28px">{{item.title}}</div>
        <div style="font-size: 14px;color: gray;line-height: 14px;margin-top: 14px">收藏于: {{item.time.slice(5,16)}}</div>
      </div>
    </div>
  </div>
  <el-pagination
      v-model:currentPage="currentPage"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
  />
</div><div v-else style="color: gray;text-align: center">还未收藏任何教程哦</div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "StarList",
  props:["type","searchPattern","keyWords","categoryId","locationId"],
  data(){
    return {
      records:[],
      currentPage:1,
      pageSize:10,
      total: 0,
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
      let url=`/star/getList/${this.type}`
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
          this.records=res.data.records
          this.total=res.data.total
        } else {
          this.$message({type:'error',message:'获取收藏列表失败'})
        }
      })
    },
    //分页大小改变
    handleSizeChange(newSize){
      this.pageSize=newSize;
      this.load();
    },
    //页码切换
    handleCurrentChange(newCurrent){
      this.currentPage=newCurrent;
      this.load();
    },
    initialize(){
      this.currentPage=1
      this.pageSize=10
      this.records=[]
      this.total=0
    },
    goToDetail(item){
      if(this.type==='blog') this.$router.push('/blog?bid='+item.contentId)
      else this.$router.push('/video?vid='+item.contentId)
    }
  },
  created() {
    this.initialize()
    this.load()
  },
}
</script>

<style scoped>
cardStyle {
  padding: 0;
}
</style>
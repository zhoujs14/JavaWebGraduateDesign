<template>
  <div v-if="data" style="display: flex;align-items: center;margin-bottom: 12px">
    <span style="color: gray;font-size: 13px">{{this.prefix}}</span>
    <el-tag :type="this.currentSelect===0?'':'info'" size="large" class="tag" @click="()=>handleSelect({id:0,name:'全部'})">全部</el-tag>
    <el-tag v-for="item in data" :type="this.currentSelect===item.id?'':'info'" size="large" class="tag" @click="()=>handleSelect(item)">{{item.name}}</el-tag>
  </div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "TagListRow",
  props:['type','prefix'],
  data(){
    return {
      data:null,
      currentSelect:0
    }
  },
  methods:{
    load(){
      let url=this.type==='category'?'/category':"/location"
      request.get(url).then(res=>{
        if(res?.code==='0'){
          this.data=res.data
        }
        else{
          this.$message({type:'error',message:`获取${this.type==='category'?'物品种类':'收纳位置'}标签失败`})
        }
      })
    },
    handleSelect(item){
      this.currentSelect=item.id
      item.type=this.type
      this.$emit('tagSelect',{tag:item})
    }
  },
  created() {
    this.load()
  }
}
</script>

<style scoped>
.tag {
  margin-left: 8px;
  cursor: pointer
}
</style>
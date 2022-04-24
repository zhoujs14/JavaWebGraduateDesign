<template>
  <div v-if="data" style="display: flex;align-items: center;margin-bottom: 12px">
    <span style="color: gray;font-size: 13px">{{this.prefix}}</span>
    <el-tag v-for="item in data" type="info" size="large" class="tag">{{item.name}}</el-tag>
  </div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "TagListRow",
  props:['type','prefix'],
  data(){
    return {
      data:null
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
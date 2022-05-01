<template>
  <el-select v-model="cateId" @change="handleSelectChange" :placeholder="this.placeholder">
    <el-option :value="0" label="默认">默认</el-option>
    <el-option v-for="item in categories" :value="item.id" :label="item.name">{{item.name}}</el-option>
  </el-select>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "CateSelect",
  data(){
    return {
      cateId:'',
      categories:[]
    }
  },
  props:['placeholder',"initialValue"],
  methods:{
    getCategories(){
      request.get('/category').then(res=>{
        if(res?.code==='0'){
          this.categories=res.data
        }
        else{
          this.$message({type:'error',message:`获取物品种类标签失败`})
        }
      })
    },
    handleSelectChange(val){
      this.$emit(
          'categorySelect',val
      )
    }
  },
  created() {
    this.getCategories()
    if(this.initialValue) this.cateId=this.initialValue
  }
}
</script>

<style scoped>

</style>
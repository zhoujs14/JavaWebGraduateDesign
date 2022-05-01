<template>
  <el-select v-model="locationId" @change="handleSelectChange">
    <el-option :value="0" label="默认">默认</el-option>
    <el-option v-for="item in locations" :value="item.id" :label="item.name">{{item.name}}</el-option>
  </el-select>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "LocationSelect",
  props:["initialValue"],
  data(){
    return {
      locationId:'',
      locations:[]
    }
  },
  methods:{
    getLocations(){
      request.get('/location').then(res=>{
        if(res?.code==='0'){
          this.locations=res.data
        }
        else{
          this.$message({type:'error',message:`获取收纳位置标签失败`})
        }
      })
    },
    handleSelectChange(val){
      this.$emit(
          'locationSelect',val
      )
    }
  },
  created() {
    this.getLocations()
    if(this.initialValue) this.locationId=this.initialValue
  }
}
</script>

<style scoped>

</style>
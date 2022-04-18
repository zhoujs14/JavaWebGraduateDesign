<template>
  <div style="display: flex;flex-direction: column;margin-bottom: 20px;border-bottom: lightgray 1px solid">
    <div style="margin-bottom: 5px">标题</div>
    <div style="display: flex">
      <el-image src="" style="width: 160px;height: 90px;margin-right: 10px"/>
      <div>简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介</div>
    </div>
    <div style="display: flex;padding: 8px 0">
      <div style="display: flex;align-items: center;margin-right: 8px"><el-icon color="darkgray" :size="20"><View /></el-icon> 1200</div>
      <div style="display: flex;align-items: center;margin-right: 8px"><img :src="require('@/assets/img/like.png')" style="width: 20px;height: 18px">500</div>
      <div style="display: flex;align-items: center;margin-right: 8px"><el-icon color="darkgray" :size="20"><star /></el-icon> 20</div>
      <div style="margin-right: 8px">作者昵称</div>
      <div>2022-4-18</div>
    </div>
    <div></div>
  </div>
</template>

<script>
export default {
  name: "Card",
  props:{
    title:String,
    picSrc:String,
    brief:String,
    like:Number,
    watched:Number
  }
}
</script>

<style scoped>
</style>
<!--主体框架-->
<template>
  <div style="width: 100%;height: 100%">
    <Header/>
      <el-row style="min-height: calc( 100vh - 50px )">
        <el-col :span="4"></el-col>
        <el-col :span="16">
          <router-view style="flex: 1"/>
        </el-col>
        <el-col :span="4"></el-col>
      </el-row>
  </div>
</template>

<script>
import Header from "./Header";
export default {
  name: "Layout",
  components: {Header}
}
</script>

<style scoped>

</style>
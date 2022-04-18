<template>
  <div style="display:flex;flex:1">
      <el-tabs type="border-card" style="flex:1">
        <el-tab-pane>
          <template #label>
            <span class="tab">
              <el-icon style="margin-right: 5px"><reading /></el-icon>
              <span>图文教程</span>
            </span>
          </template>
          <ul v-infinite-scroll="load" class="infinite-list" style="overflow: auto">
            <li v-for="i in count" :key="i" class="infinite-list-item">
              <Card/>
            </li>
          </ul>
        </el-tab-pane>
        <el-tab-pane>
          <template #label>
            <span class="tab">
              <el-icon style="margin-right: 5px"><video-play /></el-icon>
              <span>视频教程</span>
            </span>
          </template>
            <ul v-infinite-scroll="load2" class="infinite-list" style="overflow: auto">
              <li v-for="i in count2" :key="i" class="infinite-list-item">
                <Card/>
              </li>
            </ul>
        </el-tab-pane>
      </el-tabs>
  </div>
</template>

<script>
import Card from "../components/Card";
export default {
  name: "Home",
  components: {Card},
  data(){
    return {
      count:3,
      count2:3,
    }
  },
  methods:{
    load(){
      if(this.count>30) return
      this.count += 2;
    },
    load2(){
      if(this.count2>30) return
      this.count2 += 2
    }
  }
}
</script>

<style scoped>
.tab {
  display: flex;
  align-items: center;
}
.infinite-list {
  padding: 0;
  margin: 0;
  list-style: none;
}
</style>
<template>
  <div style="display:flex;flex:1;padding-top: 18px;flex-direction: column">
    <SearchBar style="margin-bottom: 14px"/>
    <div style="margin-bottom: 16px">
      <TagListRow type="category" prefix="按收纳物品分类:"/>
      <TagListRow type="location" prefix="按收纳位置分类:"/>
    </div>
    <el-tabs type="border-card" style="flex:1" v-model="activeName">
      <el-tab-pane name="blog">
        <template #label>
          <span class="tab">
            <el-icon style="margin-right: 5px"><reading /></el-icon>
            <span>图文教程</span>
          </span>
        </template>
        <CardList type="blog"/>
      </el-tab-pane>
      <el-tab-pane name="video">
        <template #label>
          <span class="tab">
            <el-icon style="margin-right: 5px"><video-play /></el-icon>
            <span>视频教程</span>
          </span>
        </template>
        <CardList type="video"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Card from "../components/Card";
import CardList from "../components/CardList";
import TagListRow from "../components/TagListRow";
import Search from "../components/SearchBar";
import SearchBar from "../components/SearchBar";

export default {
  name: "Home",
  components: {SearchBar, Search, TagListRow, CardList, Card},
  data(){
    return {
      activeName:'blog'
    }
  },
  methods:{
  }
}
</script>

<style scoped>
.tab {
  display: flex;
  align-items: center;
}
</style>
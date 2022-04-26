<template>
  <div style="display:flex;flex:1;padding-top: 18px;flex-direction: column">
    <!-- 搜索栏   -->
    <SearchBar style="margin-bottom: 14px" @handleSearch="handleSearch"/>
    <!-- 分类标签   -->
    <div style="margin-bottom: 16px">
      <TagListRow type="category" prefix="按收纳物品分类:" @tagSelect="handleTagSelect"/>
      <TagListRow type="location" prefix="按收纳位置分类:" @tagSelect="handleTagSelect"/>
    </div>
    <!-- 教程列表标签页   -->
    <el-tabs type="border-card" style="flex:1" v-model="activeName">
      <el-tab-pane name="blog">
        <template #label>
          <span class="tab">
            <el-icon style="margin-right: 5px"><reading /></el-icon>
            <span>图文教程</span>
          </span>
        </template>
        <CardList type="blog" :search-pattern="searchPattern" :key-words="keyWords" :category-id="this.cid" :location-id="this.lid"/>
      </el-tab-pane>
      <el-tab-pane name="video">
        <template #label>
          <span class="tab">
            <el-icon style="margin-right: 5px"><video-play /></el-icon>
            <span>视频教程</span>
          </span>
        </template>
        <CardList type="video" :search-pattern="searchPattern" :key-words="keyWords" :category-id="this.cid" :location-id="this.lid"/>
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
      activeName:'blog',
      searchPattern: 'title',
      keyWords: '',
      cid:0,
      lid:0
    }
  },
  methods:{
    //处理搜索参数
    handleSearch(args){
      this.searchPattern=args.searchPattern
      this.keyWords=args.keyWords
    },
    //处理搜索类别
    handleTagSelect(args){
      let tag=args.tag
      if(tag.type==='category') this.cid=tag.id
      else this.lid=tag.id
    }
  }
}
</script>

<style scoped>
.tab {
  display: flex;
  align-items: center;
}
</style>
<template>
  <el-card style="margin-top: 24px">
    <template #header>
      我的收藏
    </template>
    <el-tabs tab-position="left">
      <div class="row">
        <span>选择收纳物品种类:</span><CateSelect style="width: 140px;margin-right: 25px" @categorySelect="setCid"/>
        <span>选择收纳场所:</span><LocationSelect style="width: 140px;margin-right: 25px" @locationSelect="setLid"/>
        <SearchBar style="width: 400px" @handleSearch="handleSearch"/>
      </div>
      <el-tab-pane label="文章">
        <StarList type="blog" :category-id="cid" :location-id="lid" :key-words="keyWords" :search-pattern="searchPattern"/>
      </el-tab-pane>
      <el-tab-pane label="视频">
        <StarList type="video" :category-id="cid" :location-id="lid" :key-words="keyWords" :search-pattern="searchPattern"/>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import request from "../../utils/request";
import TagListRow from "../components/TagListRow";
import StarList from "../components/StarList";
import CateSelect from "../components/CateSelecte";
import LocationSelect from "../components/LocationSelect";
import SearchBar from "../components/SearchBar";

export default {
  name: "Favlist",
  components: {SearchBar, LocationSelect, CateSelect, StarList, TagListRow},
  data(){
    return {
      keyWords:"",
      searchPattern: 'title',
      cid:0,
      lid:0,
    }
  },
  methods: {
    setCid(val){
      this.cid=val
    },
    setLid(val){
      this.lid=val
    },
    handleSearch(args){
      this.searchPattern=args.searchPattern
      this.keyWords=args.keyWords
    }
  }
}
</script>

<style scoped>
span {
  font-size: 14px;
  color: gray;
  margin-right: 4px;
}
</style>
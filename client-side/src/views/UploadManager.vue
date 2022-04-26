<template>
  <div style="display:flex;flex:1;padding-top: 18px;flex-direction: column">
    <el-card>
      <el-tabs style="flex:1" v-model="activeName">
        <div class="row" style="font-size: 14px;margin-bottom: 22px">
            收纳位置:<LocationSelect style="margin: 0 16px 0 8px" @locationSelect="selectLocation"/>
            收纳物品:<CateSelect style="margin: 0 8px"  @categorySelect="selectCategory"/>
        </div>

        <el-tab-pane name="blog">
          <template #label>
          <span class="tab">
            <el-icon style="margin-right: 5px"><reading /></el-icon>
            <span>文章管理</span>
          </span>
          </template>
          <CardList type="blog" editable="true" :search-pattern="searchPattern" :key-words="keyWords" :category-id="this.cid"
                    :location-id="this.lid"/>
        </el-tab-pane>
        <el-tab-pane name="video">
          <template #label>
          <span class="tab">
            <el-icon style="margin-right: 5px"><video-play /></el-icon>
            <span>视频管理</span>
          </span>
          </template>
          <CardList type="video" editable="true" :search-pattern="searchPattern" :key-words="keyWords" :category-id="this.cid"
                    :location-id="this.lid"/>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import Card from "../components/Card";
import CardList from "../components/CardList";
import TagListRow from "../components/TagListRow";
import Search from "../components/SearchBar";
import SearchBar from "../components/SearchBar";
import CateSelect from "../components/CateSelecte";
import LocationSelect from "../components/LocationSelect";

export default {
  name: "UploadManager",
  components: {LocationSelect, CateSelect, SearchBar, Search, TagListRow, CardList, Card},
  data(){
    return {
      activeName:'blog',
      searchPattern: 'author',
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
    },
    selectCategory(val){
      this.cid=val
      console.log('cid',this.cid)
    },
    selectLocation(val){
      this.lid=val
      console.log('lid',this.lid)
    }
  },
  created() {
    let user=JSON.parse(sessionStorage.getItem('user'))
    if(!user) this.$router.push('/login')
    else {
      this.keyWords=user.nickName
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
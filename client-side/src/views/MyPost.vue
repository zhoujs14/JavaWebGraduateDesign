<template>

  <el-card style="margin-top: 24px">
    <template #header>
      我的讨论
    </template>
    <el-row>
      <!-- 帖子列表       -->
      <el-tabs tab-position="left" v-model="activeName" @tab-click="handleClick" style="width: 100%">
        <el-tab-pane label="全部" name="first">
          <PostList tag="all" :search-pattern="this.searchPattern" :key-words="this.keyWords"/>
        </el-tab-pane>
        <el-tab-pane label="创意讨论" name="second"><PostList tag="创意讨论" :search-pattern="this.searchPattern" :key-words="this.keyWords"/></el-tab-pane>
        <el-tab-pane label="好物分享" name="third"><PostList tag="好物分享" :search-pattern="this.searchPattern" :key-words="this.keyWords"/></el-tab-pane>
        <el-tab-pane label="物品交易" name="fourth"><PostList tag="物品交易" :search-pattern="this.searchPattern" :key-words="this.keyWords"/></el-tab-pane>
      </el-tabs>
    </el-row>
  </el-card>
</template>

<script>
import request from "../../utils/request";
import SearchBar from "../components/SearchBar";
import PostForm from "../components/PostForm";
import PostList from "../components/PostList";

const tabs=['all','创意讨论','好物分享','物品交易']

export default {
  name: "myPost",
  components: {PostList, PostForm, SearchBar},
  data(){
    return {
      searchPattern: 'authorName',
      keyWords: '',
      user:{},
      activeName:'first',
      tag:'all'
    }
  },
  created() {
    let user=JSON.parse(sessionStorage.getItem('user'))
    if(user!==null) {
      this.user = user
      this.keyWords=user.nickName
    }
    else this.$router.push('/login')
  }
}
</script>

<style scoped>
.tab {
  display: flex;
  align-items: center;
}

::v-deep img{
  max-width: 50px;
  max-height: 50px;
  object-fit: scale-down;
}
</style>
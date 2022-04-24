<template>
  <div class="card" @click='goToDetail'>
    <!-- 标题   -->
    <div style="margin-bottom: 5px">
      <el-tag type="success" v-if="data.cateName" style="margin-right: 8px">{{data.cateName}}</el-tag>
      <el-tag type="warning" v-if="data.locationName" style="margin-right: 8px">{{data.locationName}}</el-tag>
      {{data.title}}
    </div>
    <div style="display: flex">
      <!-- 封面   -->
      <div class="cover"><el-image :src="data.cover" class="cover" fit="cover"/></div>
      <!-- 简介   -->
      <div>{{data.brief}}</div>
    </div>
    <!-- 底部  -->
    <div class="bottomWrapper">
      <div class="bottomItem">
        <el-icon color="darkgray" :size="20" style="margin-right: 4px"><View /></el-icon>
        {{ data.watched }}
      </div>
      <div class="bottomItem">
        <img :src="require('@/assets/img/like.png')" class="likeIcon" style="margin: 0 4px"/>
        {{data.likesCount}}
      </div>
      <div class="bottomItem"><el-icon color="darkgray" :size="20"><star /></el-icon> 20</div>
      <div class="authorName">{{data.authorName}}</div>
      <div>{{data.time.slice(0,10)}}</div>
    </div>
    <div></div>
  </div>
</template>

<script>
export default {
  name: "Card",
  props:["data","type"],
  methods:{
    goToDetail(){
      if(!!this.data){
        console.log("type",this.type)
        let url=this.type==='blog'?"/blog?bid=":"/video?vid="
        this.$router.push(url+this.data.id)
      }
    }
  }
}
</script>

<style scoped>
  .card {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
    border-bottom: lightgray 1px solid;
    cursor: pointer;
  }
  .bottomWrapper {
    display: flex;
    padding: 8px 0;
  }
  .bottomItem {
    display: flex;
    align-items: center;
    margin-right: 8px
  }
  .likeIcon {
    width: 20px;height: 18px
  }
  .authorName {
    margin-right: 8px
  }
  .cover {
    width: 160px;
    height: 90px;
    margin-right: 10px
  }
</style>
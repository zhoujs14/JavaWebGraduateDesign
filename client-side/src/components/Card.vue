<template>
  <div class="card" @click='goToDetail'>
    <!-- 标题   -->
    <div style="margin-bottom: 5px">{{data.title}}</div>
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
  props:["data"],
  methods:{
    goToDetail(){
      if(!!this.data){
        this.$router.push("/blog?bid="+this.data.id)
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
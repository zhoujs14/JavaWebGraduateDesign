<template>
<!--  展示用卡片-->
  <div class="card" @click='goToDetail' v-if="!this.edit">
    <!-- 标题   -->
    <div style="margin-bottom: 5px">
      <el-tag type="warning" v-if="data.locationName" style="margin-right: 8px">{{data.locationName}}</el-tag>
      <el-tag type="success" v-if="data.cateName" style="margin-right: 8px">{{data.cateName}}</el-tag>
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
      <div class="bottomItem">
        <el-icon color="darkgray" :size="20" style="margin-right: 4px"><star /></el-icon>
        {{data.star}}
      </div>
      <div class="authorName">{{data.authorName}}</div>
      <div>{{data.time.slice(0,10)}}</div>
    </div>
  </div>
<!--  可编辑卡片-->
  <el-row v-else style="border-bottom: lightgray 1px solid;">
    <el-col :span="20">
      <div class="card" @click='goToDetail'>
        <div style="display: flex">
          <!-- 封面   -->
          <div class="cover"><el-image :src="data.cover" class="cover" fit="cover"/></div>
          <!-- 标题   -->
          <div class="column" style="align-items: flex-start;justify-content: space-evenly">
            {{data.title}}
            <div class="row" style="margin-top: 4px">
              <el-tag type="warning" v-if="data.locationName" style="margin-right: 8px;aliS">{{data.locationName}}</el-tag>
              <el-tag type="success" v-if="data.cateName" style="margin-right: 8px">{{data.cateName}}</el-tag>
            </div>
            <div>{{data.time}}</div>
            <div class="row">
              <div class="bottomItem">
                <el-icon color="darkgray" :size="16" style="margin-right: 4px"><View /></el-icon>
                {{ data.watched }}
              </div>
              <div class="bottomItem">
                <img :src="require('@/assets/img/like.png')" class="likeIcon2" style="margin: 0 4px"/>
                {{data.likesCount}}
              </div>
              <div class="bottomItem">
                <el-icon color="darkgray" :size="16" style="margin-right: 4px"><star /></el-icon>
                {{data.star}}
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="4" class="center">
      <el-button @click="this.dialogVisible=true">编辑</el-button>
      <el-popconfirm title="确认删除该投稿吗" @confirm="confirmDel">
        <template #reference>
          <el-button>删除</el-button>
        </template>
      </el-popconfirm>
    </el-col>
  </el-row>
<!--  弹窗-->
  <el-dialog v-model="dialogVisible" width="75%">
    <template v-if="type==='blog'"><BlogEditor :edit-blog="data" @updated="dialogVisible=false"/></template>
    <template v-else><VideoUploader :edit-video="data" @updated="dialogVisible=false"/></template>
  </el-dialog>
</template>

<script>
import request from "../../../back-stage/utils/request";
import BlogEditor from "./BlogEditor";
import VideoUploader from "./VideoUploader";

export default {
  name: "Card",
  components: {VideoUploader, BlogEditor},
  /**
   * data: 文章/视频数据
   * type: 卡片类型['blog','video]
   * edit: 是否是可编辑类型[boolean]
   */
  props:["data","type","edit"],
  data(){
    return {
      dialogVisible:false
    }
  },
  methods:{
    goToDetail(){
      if(!!this.data){
        let url=this.type==='blog'?"/blog?bid=":"/video?vid="
        this.$router.push(url+this.data.id)
      }
    },
    confirmDel(){
      let url=this.type==='blog'?'/blog':'/video'
      request.delete(`${url}/${this.data.id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        window.location.reload()
      })
    }
  }
}
</script>

<style scoped>
  .card {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
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
  .likeIcon2 {
    width: 16px;height: 14px
  }
  .authorName {
    margin-right: 8px
  }
  .cover {
    width: 208px;
    height: 117px;
    margin-right: 10px
  }
</style>
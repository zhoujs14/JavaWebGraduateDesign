<template>
  <!-- 搜索栏  -->
  <div style="margin-top: 24px">
    <SearchBar @handleSearch="handleSearch"/>
  </div>
  <el-row style="margin-top: 24px">
    <!-- 用户信息卡片   -->
    <el-col :span="4">
      <el-card style="margin-right: 8px;display: flex;flex-direction: column;align-items: center">
        <div style="display: flex;justify-content: center;margin-bottom: 8px"><el-avatar :src="user.avatarSrc"/></div>
        <div>{{user.nickName}}</div>
      </el-card>
    </el-col>

    <!-- 中央区域   -->
    <el-col :span="20">
      <el-card>
        <template #header>
          讨论区
        </template>
        <!-- 帖子列表       -->
        <template v-if="this.records.length>0">
          <el-row  v-for="item in records" style="padding: 4px 8px">
            <el-col :span="16">
              <a :href="`/postDetail?pid=${item.id}`" style="font-size: 17px;margin-bottom: 14px">{{item.title}}</a>
              <div v-html="item.content" style="font-size: 8px;overflow: hidden;max-height: 90px"/>
            </el-col>
            <el-col :span="4" class="center" style="font-size: 14px;color: gray">{{item.authorName}}</el-col>
            <el-col :span="4" class="center" style="font-size: 14px;color: gray">{{item.time.slice(0,16)}}</el-col>
            <el-divider style="margin-bottom: 4px"/>
          </el-row>
        </template>
        <div v-else class="center" style="color: gray">暂无数据</div>
        <el-pagination
            v-model:currentPage="currentPage"
            :page-size="pageSize"
            :page-sizes="[5, 10, 20]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </el-card>
      <!-- 发帖   -->
      <el-card style="margin-top: 24px">
        <PostForm @onSubmit="load"/>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import request from "../../utils/request";
import SearchBar from "../components/SearchBar";
import PostForm from "../components/PostForm";



export default {
  name: "Post",
  components: {PostForm, SearchBar},
  data(){
    return {
      searchPattern: 'title',
      keyWords: '',
      date: new Date(),
      user:{},
      records:[],
      currentPage:1,
      pageSize:10,
      total: 0,
    }
  },
  methods:{
    load(){
      request.get('/post',{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          searchPattern:this.searchPattern,
          keyWords:this.keyWords,
        }
      }).then(res=>{
        if(res?.code==='0'){
          this.records=res.data.records
          this.total=res.data.total
        } else {
          this.$message({type:'error',message:'获取收藏列表失败'})
        }
      })
    },
    //处理搜索参数
    handleSearch(args){
      console.log("search")
      this.searchPattern=args.searchPattern
      this.keyWords=args.keyWords
      this.load()
    },
    //分页大小改变
    handleSizeChange(newSize){
      this.pageSize=newSize;
      this.load();
    },
    //页码切换
    handleCurrentChange(newCurrent){
      this.currentPage=newCurrent;
      this.load();
    },
    initialize(){
      this.currentPage=1
      this.pageSize=10
      this.records=[]
      this.total=0
    },
    goToDetail(item){
      if(this.type==='blog') this.$router.push('/blog?bid='+item.contentId)
      else this.$router.push('/video?vid='+item.contentId)
    },
  },
  created() {
    let user=JSON.parse(sessionStorage.getItem('user'))
    this.user=user
    this.initialize()
    this.load()
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
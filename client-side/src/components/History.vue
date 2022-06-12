<template>
  <el-popover trigger="hover" width="400px" @show="load" v-model="visible">
    <template #reference>
      <div class="click">
        <el-icon :size="20" color="gray"><clock /></el-icon>
        <span class="grayText">历史</span>
      </div>
    </template>
    <el-scrollbar max-height="40vh">
      <template v-if="records.length>0" v-for="(item,index) in records">
        <div class="row" style="cursor: pointer" @click="goToDetail(item)">
          <div style="margin-right: 12px"><el-image :src="item.cover" class="cover"/></div>
          <div class="info">
            <div class="title">
              {{item.title}}
            </div>
            <div>
              <div style="margin-bottom: 4px">{{item.authorName}}</div>
              <div>{{item.time.slice(5,16)}}</div>
            </div>
          </div>
        </div>
        <el-divider v-if="index+1!=records.length"/>
      </template>
      <div v-else class="center" style="color: gray">暂时没有浏览记录哦</div>
    </el-scrollbar>
  </el-popover>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "History",
  data(){
    return {
      records:[],
      visible: false
    }
  },
  watch: {
    '$route' (to, from) {
      this.$router.go(0);
    }
  },
  methods:{
    load(){
      request.get('/watch').then(res=>{
        if(res?.code==='0'){
          this.records=res.data
        }
        else {
          this.$message({type:'error',message:'获取历史记录失败:'+res?.msg})
        }
      })
    },
    goToDetail(item){
      console.log("goto"+item.type+";"+item.contentId)
      if(item.type==='blog') this.$router.push('/blog?bid='+item.contentId)
      else this.$router.push('/video?vid='+item.contentId)
      this.visible=false
    }
  },
  created() {
    this.load()
  }
}
</script>

<style scoped>
.title{
  font-size: 15px;
  line-height: 15px;
  max-height: 30px;
  overflow: hidden;
  font-weight: bold;
}
.click {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  margin-left: 15px;
}
.grayText {
  font-size: 13px;
  color: gray
}
.cover {
  width: 160px;
  height: 90px;
}
.info{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 90px
}
</style>
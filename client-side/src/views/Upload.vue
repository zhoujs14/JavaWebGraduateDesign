<template>
  <div style="flex: 1">
    <el-card style="width: 90%;margin: 24px auto 0">
      <template #header>
        <span>上传投稿</span>
      </template>
      <el-tabs style="flex:1">
        <el-tab-pane>
          <template #label>
            <span class="tab">
              <el-icon style="margin-right: 5px"><reading /></el-icon>
              <span>图文教程</span>
            </span>
          </template>
          <BlogEditor/>
        </el-tab-pane>
        <el-tab-pane>
          <template #label>
            <span class="tab">
              <el-icon style="margin-right: 5px"><video-play /></el-icon>
              <span>视频教程</span>
            </span>
          </template>
          <VideoUploader/>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import request from "../../utils/request";
import BlogEditor from "../components/BlogEditor";
import VideoUploader from "../components/VideoUploader";

export default {
  name: "Upload",
  components: {VideoUploader, BlogEditor},
  data(){
    return {
      user:{
        avatarSrc:""
      }
    }
  },
  created() {
    let user=JSON.parse(sessionStorage.getItem("user"))
    if(!user){
      this.$router.push("/login")
    }
    else{
      this.user=user
    }
  },
  methods:{
    //上传服务器成功
    fileUploadSuccess(res){
      if(res?.data?.length>0) {
        this.user.avatarSrc=res.data
        //保存头像地址
        request.post("/user/avatar",this.user).then(res=>{
          if(res?.code==='0') {
            sessionStorage.setItem("user", JSON.stringify(this.user))
            this.$message({type:"success",message:"头像更新成功"})
          }
          else{
            this.$message({type:"error",message:"头像更新失败:"})
          }
        })
      }
    },
    save(){
      request.put("/user",this.user).then(res=> {
        let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
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
<template>
  <div>
    <el-form :model="video" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="video.title" style="width: 80%"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="8">
          <el-form-item label="封面">
            <el-upload :show-file-list="false"
                       :on-success="uploadCoverSuccess"
                       :before-upload="beforeCoverUpload"
                       action="http://localhost:9090/files/upload"
                       class="coverUploader"
            >
              <img v-if="video.cover" :src="video.cover" class="uploadIcon"/>
              <el-icon v-else class="uploadIcon"><Plus/></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="简介">
            <el-input
                v-model="video.brief"
                maxlength="200"
                placeholder="Please input"
                show-word-limit
                type="textarea"
                rows="9"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="上传视频">
        <el-upload
            class="upload-demo"
            drag
            action="http://localhost:9090/files/upload"
            multiple
            :on-success="uploadVideoSuccess"
            :before-upload="beforeVideoUpload"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            拖拽文件到这里 或 <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              请上传500MB以内的mp4文件
            </div>
          </template>
        </el-upload>
      </el-form-item>
      <el-divider/>
    </el-form>
    <div style="display: flex;justify-content: flex-end">
      <el-button type="info" @click="saveLocal">本地暂存</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "VideoUploader",
  data(){
    return {
      video:{
        cover:null,
        brief:"",
        title:"",
      }
    }
  },
  methods:{
    //上传封面成功
    uploadCoverSuccess(res){
      if(res?.data?.length>0) {

        this.video.cover=res.data
        this.$message({type:"success",message:"上传成功"})
      }
    },
    //判断封面文件格式与大小
    beforeCoverUpload(rawFile){
      if (rawFile.type !== 'image/jpeg' && rawFile.type!== 'image/png'&&rawFile.type!=='image/x-png') {
        this.$message.error('请上传png或jpg类型图片')
        return false
      } else if (rawFile.size / 1024 / 1024 > 2) {
        this.$message.error('图片尺寸过大')
        return false
      }
      return true
    },
    //上传视频成功
    uploadVideoSuccess(res){
      if(res?.data?.length>0) {
        this.video.src=res.data
        this.$message({type:"success",message:"上传成功"})
      }
    },
    //判断视频文件格式与大小
    beforeVideoUpload(rawFile){
      if (rawFile.type!='video/mp4') {
        this.$message.error('请上传mp4文件')
        return false
      } else if (rawFile.size / 1024 / 1024 > 500) {
        this.$message.error('视频过大')
        return false
      }
      return true
    },
    save(){
      request.post("/video",this.video).then(res => {
        if(res?.code==='0'){
          this.$message({type:'success',message:'投稿成功'})
          sessionStorage.setItem("video",null)
          this.video={
            cover:null,
            brief:"",
            title:"",
          }
        }
        else {
          this.$message({type:'error',message:'投稿失败,错误信息：'+res?.msg})
        }
      })
    },
    saveLocal(){
      sessionStorage.setItem("video",JSON.stringify(this.video))
      this.$message({type:'success',message:'保存成功'})
    }
  },
  created() {
    let localVideo=JSON.parse(sessionStorage.getItem("video"))
    if(!!localVideo) this.video=localVideo
  }
}
</script>

<style scoped>
.coverUploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  width: 300px;
  height: 200px;
  cursor: pointer;
}
.coverUploader:hover {
  border-color: var(--el-color-primary);
}
.uploadIcon {
  width: 300px;
  height: 200px;
  text-align: center;
  font-size: 28px;
}
</style>
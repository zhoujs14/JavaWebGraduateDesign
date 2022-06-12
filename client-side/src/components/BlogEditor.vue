<template>
  <div>
    <el-form :model="blog" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="blog.title" style="width: 80%"></el-input>
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
              <img v-if="blog.cover" :src="blog.cover" class="uploadIcon"/>
              <el-icon v-else class="uploadIcon"><Plus/></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="简介">
            <el-input
                v-model="blog.brief"
                maxlength="200"
                placeholder="Please input"
                show-word-limit
                type="textarea"
                rows="9"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="收纳物品分类">
            <CateSelect :initial-value="editBlog?.cateId||0" @categorySelect="selectCate"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收纳位置分类">
            <LocationSelect :initial-value="editBlog?.locationId||0" @locationSelect="selectLocation"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider/>
      <div id="toolbar-container"></div>
      <div id="editor-container" style="height: 60vh"></div>
    </el-form>
    <div style="display: flex;justify-content: flex-end">
      <el-button type="info" @click="saveLocal">本地暂存</el-button>
      <el-button type="primary" @click="save">提交文章</el-button>
    </div>
  </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css'
import { createEditor, createToolbar } from '@wangeditor/editor'
import request from "../../utils/request";
import CateSelect from "./CateSelecte";
import LocationSelect from "./LocationSelect";

let editorConfig= {}
let editor,toolbar

export default {
  name: "BlogEditor",
  components: {LocationSelect, CateSelect},
  props:['editBlog'],
  data(){
    return {
      blog:{
        cover:null,
        brief:"",
        title:"",
        cateId:'',
        locationId:''
      },
      categories:[],
      locations:[]
    }
  },
  methods:{
    //上传封面成功
    uploadCoverSuccess(res){
      if(res?.data?.length>0) {
        this.blog.cover=res.data
        this.$message({type:"success",message:"上传成功"})
      }
    },
    //判断文件格式与大小
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
    save(){
      this.blog.content=editor.getHtml()  //获取编辑器内容到表单
      if(!!this.editBlog) {
        request.put("/blog",this.blog).then(res => {
          if(res?.code==='0') {
            this.$message({type: 'success', message: '编辑成功'})
            this.$emit('updated')
          }
          else this.$message({type:'error',message:'编辑失败,错误信息：'+res?.msg})
        })
      }
      else {
        request.post("/blog",this.blog).then(res => {
          if(res?.code==='0'){
            this.$message({type:'success',message:'投稿成功'})
            sessionStorage.setItem("blog",null)
            this.blog={
              cover:null,
              brief:"",
              title:"",
            }
            this.initializeEditor()
          }
          else {
            this.$message({type:'error',message:'投稿失败,错误信息：'+res?.msg})
          }
        })
      }
    },
    saveLocal(){
      this.blog.content=editor.getHtml()
      sessionStorage.setItem("blog",JSON.stringify(this.blog))
      this.$message({type:'success',message:'保存成功'})
    },
    //初始化编辑器 @param:初始内容
    initializeEditor(content){
      //在弹窗内创建编辑器
      this.$nextTick(()=>{
        editorConfig={MENU_CONF: {}}
        editorConfig.placeholder = '请输入内容'
        //设置图片上传api
        editorConfig.MENU_CONF['uploadImage'] = {
          server: 'http://localhost:9090/files/blogImageUpload',
          fieldName: 'file', //设置上传参数名称，需要与后台接受参数名一样
        }

        // 创建编辑器
        editor?.destroy()
        editor= createEditor({
          html: content||"",
          selector: '#editor-container',
          config: editorConfig,
          mode: 'default'
        })

        // 创建工具栏
        toolbar?.destroy()
        toolbar = createToolbar({
          editor,
          selector: '#toolbar-container',
          mode: 'default'
        })
      })
    },
    selectCate(val){
      this.blog.cateId=val
    },
    selectLocation(val){
      this.blog.locationId=val
    },
  },
  created() {
    //若传来初始内容
    if(!!this.editBlog&&!!this.editBlog?.content){
      this.initializeEditor(this.editBlog.content)
      this.blog=this.editBlog
    }
    else {
      let localBlog=JSON.parse(sessionStorage.getItem("blog"))
      if(!!localBlog) this.blog=localBlog
      this.initializeEditor(localBlog?.content)
    }
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
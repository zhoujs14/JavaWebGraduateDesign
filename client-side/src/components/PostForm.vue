<template>
  <!-- 发帖表单  -->
    <el-form v-model="form">
      <el-row>
        <el-col :span="5"><el-form-item v-if="this.parentId==null" label="标签" style="width: 200px">
          <el-select v-model="form.tag">
            <el-option value="创意讨论">创意讨论</el-option>
            <el-option value="好物分享">好物分享</el-option>
            <el-option value="物品交易">物品交易</el-option>
          </el-select>
        </el-form-item></el-col>
        <el-col :span="19">
          <el-form-item label="标题" v-if="this.parentId==null">
            <el-input v-model="form.title" placeholder="请输入标题"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <div style="padding: 2px;margin-bottom:12px;border: lightgray 1px solid">
        <div id="toolbar-container"></div>
        <div id="editor-container" style="min-height: 20vh"></div>
      </div>
      <div class="row" style="justify-content: space-between">
        <el-button type="primary" @click="save" :disabled="formDisabled">发帖</el-button>
        <el-icon :size="20" style="cursor: pointer" @click="this.reset"><delete/></el-icon>
      </div>
    </el-form>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css'
import { createEditor, createToolbar } from '@wangeditor/editor'
import request from "../../utils/request";

let editorConfig= {}
let editor,toolbar

export default {
  name: "PostForm",
  props:["parentId","focus","formDisabled"],
  data(){
    return {
      form:{
        title:'',
        content:'',
        tag:"创意讨论"
      }
    }
  },
  watch:{
    focus:{
      handler(){
        editor.focus()
      }
    }
  },
  methods:{
    //初始化编辑器 @param:初始内容
    commentditor(content){
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
          mode: 'simple'
        })

        // 创建工具栏
        toolbar?.destroy()
        toolbar = createToolbar({
          editor,
          selector: '#toolbar-container',
          mode: 'simple'
        })
      editor.focus()
      })
    },
    reset(){
      this.form={
        title: '',
        content: ''
      }
      this.commentditor()
    },
    save(){
      this.form.content=editor.getHtml()
      this.form.parentId=this.parentId||null
      request.post('/post',this.form).then(res=>{
        if(res?.code==='0'){
          this.$message({type:'success',message:'发帖成功'})
          this.reset()
          window.location.reload()
        }
        else {
          this.$message({type:'error',message:'发帖失败;'+res?.msg})
        }
      })
      this.$emit('onSubmit')
    }
  },
  created() {
    this.commentditor()
  },
}
</script>

<style scoped>

</style>
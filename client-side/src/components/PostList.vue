<!--我的讨论-帖子列表-->
<template>
  <div v-if="this.records.length>0" style="width: 100%;">
    <el-row  v-for="item in records" style="padding: 4px 8px">
      <el-col :span="16" style="cursor: pointer" @click="this.$router.push(`/postDetail?pid=${item.id}`)">
        <div class="row" style="align-items: center;margin-bottom: 8px">
          <el-tag style="margin-right: 4px" :type="getTagType(item.tag)">{{item.tag}}</el-tag>
          <a :href="`/postDetail?pid=${item.id}`" style="font-size: 17px">{{item.title}}</a>
        </div>
        <!--预览内容-->
        <div v-html="item.content" style="font-size: 8px;overflow: hidden;max-height: 90px"/>
      </el-col>
      <el-col :span="4" class="center" style="font-size: 14px;color: gray">{{item.time.slice(0,16)}}</el-col>
      <el-col :span="4" class="center" style="font-size: 14px;color: gray">
        <el-button @click="handleEdit(item)" type="text" size="small">编辑</el-button>
        <!--          删除弹窗-->
        <el-popconfirm title="确认删除吗？" @confirm="handleDelete(item.id)">
          <template #reference>
            <el-button type="text" size="small">删除</el-button>
          </template>
        </el-popconfirm>
      </el-col>
      <el-divider style="margin-bottom: 4px"/>
    </el-row>
<!--    分页-->
    <el-pagination
        v-model:currentPage="currentPage"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
    <el-dialog v-model="dialogVisible" title="" width="70%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="form.title" style="width: 80%"></el-input>
        </el-form-item>
        <div id="toolbar-container"></div>
        <div id="editor-container" style="height: 60vh"></div>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确认</el-button>
          </span>
      </template>
    </el-dialog>
  </div>
  <div v-else class="center">暂无数据</div>
</template>

<script>
import request from "../../utils/request";
import '@wangeditor/editor/dist/css/style.css'
import { createEditor, createToolbar } from '@wangeditor/editor'

let editorConfig= {}
let editor,toolbar

export default {
  name: "postList",
  props:["tag","searchPattern","keyWords"],
  data(){
    return {
      records:[],
      currentPage:1,
      pageSize:10,
      total: 0,
      form:{

      },
      dialogVisible:false
    }
  },
  methods:{
    //初始化编辑器 @param:初始内容
    initializeEditor(content){
      //在弹窗内创建编辑器
      this.$nextTick(()=>{
        editorConfig={MENU_CONF: {}}
        editorConfig.placeholder = '请输入内容'
        //设置图片上传api
        editorConfig.MENU_CONF['uploadImage'] = {
          server: 'http://localhost:9090/files/postImageUpload',
          fieldName: 'file', //设置上传参数名称，需要与后台接受参数名一样
        }

        // 创建编辑器
        editor?.destroy()
        editor= createEditor({
          html: content||"",
          selector: '#editor-container',
          config: editorConfig,
          mode: 'default' // 或 'simple' 参考下文
        })

        // 创建工具栏
        toolbar?.destroy()
        toolbar = createToolbar({
          editor,
          selector: '#toolbar-container',
          mode: 'default' // 或 'simple' 参考下文
        })
      })
    },
    load(){
      request.get('/post',{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          searchPattern:this.searchPattern,
          keyWords:this.keyWords,
          tag:this.tag
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
    //根据tag获取标签样式
    getTagType(tag){
      if(tag==='好物分享') return 'success'
      else if(tag==='物品交易') return 'warning'
      else return ''
    },
    handleEdit(item){
      this.form=item
      this.dialogVisible=true
      this.initializeEditor(this.form?.content)
    },
    handleDelete(id){
      request.delete(`/post/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    },
    save(){
      this.form.content=editor.getHtml()  //获取编辑器内容到表单
      //修改文章
      if(this.form.id){
        request.put("/post",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
          this.dialogVisible=false
        })
      }
    }
  },
  created() {
    this.initialize()
    this.load()
  }
}
</script>

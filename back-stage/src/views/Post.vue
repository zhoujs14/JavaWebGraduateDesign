<template>
  <div style="width: 450px;margin-bottom: 12px;padding: 10px">
    <!--    搜索区域-->
    <SearchBar style="width: 450px;margin-bottom: 12px" @handleSearch="handleSearch"/>
    <!--    表格-->
    <el-table :data="tableData" border stripe style="width: 100%" fit>
      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="authorId" label="作者id" />
      <el-table-column prop="authorName" label="作者昵称" />
      <el-table-column prop="time" label="发布时间" />
      <el-table-column label="回帖" >
        <template #default="scope">
          <el-button @click="repost(scope.row)" type="text" size="small">查看回帖({{scope.row.repostCount||0}})</el-button>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="300px">
        <template #default="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="read(scope.row)" type="text" size="small">查看</el-button>
          <!--          删除弹窗-->
          <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="text" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <!--      页码选择-->
      <el-pagination
          v-model:currentPage="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
<!--      新增、修改弹窗-->
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
      <el-dialog v-model="previewVisible" title="预览" width="70%">
        <el-card>
          <div v-html="preViewContent"></div>
        </el-card>
      </el-dialog>

<!--      回帖列表-->
      <el-dialog ref="repostDialog" v-model="repostVisible" title="回帖列表" width="70%">
        <el-card>
          <el-table :data="currentPost.children" border stripe style="width: 100%" fit>
            <el-table-column prop="id" label="ID" sortable />
            <el-table-column prop="authorId" label="作者id" />
            <el-table-column prop="authorName" label="作者昵称" />
            <el-table-column prop="time" label="发布时间" />
            <el-table-column label="内容">
              <template #default="props">
                <div v-html="props.row.content"></div>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="300px">
              <template #default="scope">
                <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
                <el-button @click="preview(scope.row)" type="text" size="small">查看</el-button>
                <!--          删除弹窗-->
                <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.id)">
                  <template #reference>
                    <el-button type="text" size="small">删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";
import { Search } from '@element-plus/icons-vue'
import '@wangeditor/editor/dist/css/style.css'
import { createEditor, createToolbar } from '@wangeditor/editor'
import SearchBar from "../components/SearchBar";

let editorConfig= {}
let editor,toolbar


export default {
  name: 'Post',
  components: {SearchBar},
  data(){
    return {
      form:{},
      dialogVisible:false, //弹窗是否可见
      repostVisible:false, //回帖是否可见
      previewVisible:false, //预览是否可见
      tableData: [],
      total:this.tableData?.length||0,
      keyWords:"",
      searchIcon:Search,
      currentPage:1,
      pageSize:10,
      currentPost:{
        title:"",
        content:"default"
      },
      preViewContent:""
    }
  },
  created() {
    this.load();
  },
  methods:{
    initialize(){
      this.currentPage=1
      this.total=0
    },
    //获取分页数据
    load(){
      request.get("/post",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          keyWords:this.keyWords,
          searchPattern:this.searchPattern
        }
      }).then(res=>{
        this.tableData=res.data.records;
        this.total=res.data.total;
      })
    },
    handleSearch(args){
      this.searchPattern=args.searchPattern
      this.keyWords=args.keyWords
      this.initialize()
      this.load()
    },
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
    //新增文章弹窗
    add(){
      //获取当前登录用户
      let user=JSON.parse(sessionStorage.getItem("user"));
      if(user&&!!user.id){
        this.dialogVisible=true;
        this.form={
          authorId:user.id //将用户id存入表单
        };
        this.initializeEditor()
      }
      //获取登录用户失败，提示登录
      else {
        this.$message({type:"error",message:"请先登录"});
      }
    },
    //保存修改或新增用户
    save(){
      this.form.content=editor.getHtml()  //获取编辑器内容到表单
      //修改文章
      if(this.form.id){
        request.put("/post",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      //新增文章
      else {
        request.post("/post",this.form).then(res => {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      this.dialogVisible = false
    },
    read(row){
      //跳转详情页
      window.open("http://localhost:9875/postDetail?pid="+row.id,"_blank")
    },
    repost(row){
      request.get("/post/"+row.id).then(res=>{
        if(res?.code==='0'){
          this.currentPost=res.data
          this.repostVisible=true
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
    //编辑
    handleEdit(row){
      this.form=JSON.parse(JSON.stringify(row)) //将行对象深拷贝到form
      this.dialogVisible=true //显示表单
      this.initializeEditor(this.form?.content)
    },
    //预览
    preview(row){
      this.preViewContent=row.content
      this.previewVisible=true
    },
    handleDelete(id){
      request.delete(`/post/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

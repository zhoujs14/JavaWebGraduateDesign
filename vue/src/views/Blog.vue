<template>
  <div style="padding:10px">
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="keyWords" placeholder="请输入关键字" clearable style="width: 20%"/>
      <el-button style="margin-left: 10px" type="primary" :icon="searchIcon" @click="load">查询</el-button>
    </div>
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>
    <!--    表格-->
    <el-table :data="tableData" border stripe style="width: 100%" fit>
      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="authorId" label="作者id" />
      <el-table-column prop="time" label="发布时间" />
      <el-table-column prop="cateID" label="类别id" />
      <el-table-column prop="cateName" label="类别名称" />
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
      <!--      用户表单-->
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
<!--      查看文章-->
      <el-dialog ref="contentDialog" v-model="contentVisible" :title="this.currentBlog.title" width="70%">
        <el-card>
          <div v-html="currentBlog.content"></div>
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

let editorConfig= {}
let editor,toolbar


export default {
  name: 'Blog',
  components: {},
  data(){
    return {
      form:{},
      dialogVisible:false, //新增用户弹窗是否可见
      contentVisible:false, //文章详情是否可见
      tableData: [],
      total:this.tableData?.length||0,
      keyWords:"",
      searchIcon:Search,
      currentPage:1,
      pageSize:10,
      currentBlog:{
        title:"",
        content:"default"
      }
    }
  },
  created() {
    this.load();
  },
  methods:{
    //获取分页数据
    load(){
      request.get("/blog",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          keyWords:this.keyWords
        }
      }).then(res=>{
        this.tableData=res.data.records;
        this.total=res.data.total;
      })
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
      if(user&&user?.id>0){
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
        request.put("/blog",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      //新增文章
      else {
        request.post("/blog",this.form).then(res => {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      this.dialogVisible = false
    },
    read(row){
      this.contentVisible=true
      this.$nextTick(()=>{
        this.currentBlog=JSON.parse((JSON.stringify(row)))
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
    handleDelete(id){
      request.delete(`/blog/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

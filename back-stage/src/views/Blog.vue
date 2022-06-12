<template>
  <div style="padding:10px">
    <!--    搜索区域-->
    <SearchBar style="width: 450px;margin-bottom: 12px" @handleSearch="handleSearch"/>
    <!--    表格-->
    <el-table :data="tableData" border stripe style="width: 100%" fit>
      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="authorId" label="作者id" />
      <el-table-column prop="authorName" label="作者昵称" />
      <el-table-column prop="time" label="发布时间" />
      <el-table-column prop="cateName" label="类别名称" />
      <el-table-column prop="locationName" label="位置名称" />
      <el-table-column prop="watched" label="浏览量" />
      <el-table-column prop="likesCount" label="点赞量" />
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
      <el-dialog v-model="dialogVisible" title="" width="70%" destroy-on-close>
        <el-card>
          <BlogEditor :edit-blog="currentBlog" @updated="onUpdated"/>
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
import BlogEditor from "../components/BlogEditor";
import SearchBar from "../components/SearchBar";

let editorConfig= {}
let editor,toolbar


export default {
  name: 'Blog',
  components: {SearchBar, BlogEditor},
  data(){
    return {
      form:{},
      dialogVisible:false, //新增弹窗是否可见
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
    initialize(){
      this.currentPage=1
      this.total=0
    },
    //获取分页数据
    load(){
      request.get("/blog",{
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
    //保存修改或
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
      this.dialogVisible = false
    },
    read(row){
      //跳转详情页
      window.open("http://localhost:9875/blog?bid="+row.id,"_blank")
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
      this.currentBlog=row
      this.dialogVisible=true //显示表单
    },
    onUpdated(){
      this.dialogVisible=false
      this.currentBlog={}
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

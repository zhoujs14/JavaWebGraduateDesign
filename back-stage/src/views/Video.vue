<template>
  <div style="padding:10px">
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="keyWords" placeholder="请输入关键字" clearable style="width: 20%"/>
      <el-button style="margin-left: 10px" type="primary" :icon="searchIcon" @click="load">查询</el-button>
    </div>
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
          <el-button @click="watch(scope.row)" type="text" size="small">查看</el-button>
          <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
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
      <!--      表单-->
      <el-dialog v-model="dialogVisible" title="" width="70%" destroy-on-close>
        <VideoUploader :edit-video="currentVideo"/>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";
import { Search } from '@element-plus/icons-vue'
import VideoUploader from "../components/VideoUploader";

export default {
  name: 'Video',
  components: {VideoUploader},
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
      currentVideo:{
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
      request.get("/video",{
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
        request.put("/video",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      //新增文章
      else {
        request.post("/video",this.form).then(res => {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      this.dialogVisible = false
    },
    watch(row){
      //跳转详情页
      window.open("http://localhost:9875/video?vid="+row.id,"_blank")
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
    handleEdit(row){
      this.currentVideo=row
      this.dialogVisible=true
    },
    handleDelete(id){
      request.delete(`/video/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

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
      <el-table-column prop="authorId" label="作者id" sortable />
      <el-table-column prop="content" label="内容" />
      <el-table-column prop="time" label="发布时间" sortable/>
      <el-table-column prop="type" label="类型" :filters="[
        { text: 'blog', value: 'blog' },
        { text: 'video', value: 'video' },
        { text: 'post', value: 'post' },
      ]" :filter-method="filterHandler"
      />
      <el-table-column prop="rootId" label="所属主体id" sortable/>
      <el-table-column prop="parentId" label="父评论id" sortable/>
      <el-table-column fixed="right" label="操作" width="300px">
        <template #default="scope">
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
      <!--      用户表单-->
      <el-dialog v-model="dialogVisible" title="" width="70%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="内容">
            <el-input v-model="form.content" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确认</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";
import { Search } from '@element-plus/icons-vue'


export default {
  name: 'Comment',
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
      currentComment:{
        content:""
      }
    }
  },
  created() {
    this.load();
  },
  methods:{
    //获取分页数据
    load(){
      request.get("/comment/admin",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          keyWords:this.keyWords,
        }
      }).then(res=>{
        this.tableData=res.data.records;
        this.total=res.data.total;
      })
    },
    filterHandler(value,row){
      return row.type===value
    },
    //保存修改或新增用户
    save(){
      //修改
      if(this.form.id){
        request.put("/comment",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
          this.$message(options)
          this.load();
        })
      }
      this.dialogVisible = false
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
    },
    handleDelete(id){
      request.delete(`/comment/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

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
      <el-table-column prop="name" label="类别名" />
      <el-table-column fixed="right" label="操作">
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
<!-- 表单-->
      <el-dialog v-model="dialogVisible" title="" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="类别名称">
            <el-input v-model="form.name" style="width: 80%"></el-input>
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

const tableData = [];

import { Search } from '@element-plus/icons-vue'

export default {
  name: 'User',
  components: {},
  data(){
    return {
      form:{},
      dialogVisible:false, //新增弹窗是否可见
      tableData: tableData,
      total:tableData.length||0,
      keyWords:"",
      searchIcon:Search,
      currentPage:1,
      pageSize:10,
    }
  },
  created() {
    this.load();
  },
  methods:{
    //获取分页数据
    load(){
      request.get("/category").then(res=>{
        this.tableData=res.data;
      })
    },
    //新增用户弹窗
    add(){
      this.dialogVisible=true;
      this.form={name:''}; //清空之前的表单域
    },
    //保存修改或新增用户
    save(){
      //修改用户
      if(this.form.id){
        request.put("/category",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res.msg}
          this.$message(options)
          this.load();
        })
      }
      //新增用户
      else {
        request.post("/category",this.form).then(res => {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res.msg}
          this.$message(options)
          this.load();
        })
      }

      this.dialogVisible = false
    },
    //编辑
    handleEdit(row){
      this.form=JSON.parse(JSON.stringify(row)) //将行对象深拷贝到form
      console.log(this.form)
      this.dialogVisible=true; //显示表单
    },
    handleDelete(id){
      request.delete(`/category/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

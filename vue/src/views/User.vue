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
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickName" label="昵称" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column label="角色">
        <template #default="scope">
          <span v-if="scope.row.role===1">管理员</span>
          <span v-if="scope.row.role===2">普通用户</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
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
      <el-dialog v-model="dialogVisible" title="" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="form.username" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" style="width: 80%"></el-input>
          </el-form-item>
         <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width: 80%"></el-input>
          </el-form-item>
         <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%"></el-input>
         </el-form-item>
         <el-form-item label="性别">
            <el-radio v-model="form.gender" label="男">男</el-radio>
            <el-radio v-model="form.gender" label="女">女</el-radio>
            <el-radio v-model="form.gender" label="未知">未知</el-radio>
         </el-form-item>
          <el-form-item label="账号类型">
            <el-radio v-model="form.role" :label="1" :disabled="true">管理员</el-radio>
            <el-radio v-model="form.role" :label="2" :disabled="true">普通用户</el-radio>
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
      dialogVisible:false, //新增用户弹窗是否可见
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
      request.get("/user",{
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
    //新增用户弹窗
    add(){
      this.dialogVisible=true;
      this.form={gender:'未知',role:2}; //清空之前的表单域
    },
    //保存修改或新增用户
    save(){
      //修改用户
      if(this.form.id){
        request.put("/user",this.form).then(res=> {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res.msg}
          this.$message(options)
          this.load();
        })
      }
      //新增用户
      else {
        request.post("/user",this.form).then(res => {
          let options=res?.code==='0'?{type:"success",message:"编辑成功"}:{type:"error",message:"编辑失败,错误信息:"+res.msg}
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
      console.log(this.form)
      this.dialogVisible=true; //显示表单
    },
    handleDelete(id){
      request.delete(`/user/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

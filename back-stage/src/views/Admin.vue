<template>
  <div style="padding:10px" v-if="level>-1&&level<3">
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="keyWords" placeholder="请输入用户名" clearable style="width: 20%"/>
      <el-button style="margin-left: 10px" type="primary" :icon="searchIcon" @click="load">查询</el-button>
    </div>
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add" v-if="level<2">新增管理员</el-button>
    </div>
    <!--    表格-->
    <el-table :data="tableData" border stripe style="width: 100%" fit>
      <el-table-column prop="id" label="ID" sortable />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="level" label="权限等级" sortable/>
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
      <!--      新增管理员表单-->
      <el-dialog v-model="dialogVisible"  title="" width="30%">
        <el-form :model="form" label-width="120px" ref="form" :rules="rules">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" style="width: 80%" placeholder="输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" style="width: 80%" placeholder="输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="权限等级" prop="level">
            <el-radio v-model="form.level" :label="0" :disabled="this.level>0">lv0</el-radio>
            <el-radio v-model="form.level" :label="1" :disabled="this.level>0">lv1</el-radio>
            <el-radio v-model="form.level" :label="2" :disabled="this.level>1">lv2</el-radio>
            <el-radio v-model="form.level" :label="3" :disabled="this.level>1">lv3</el-radio>
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
  name: 'Admin',
  components: {},
  data(){
    return {
      form:{},
      dialogVisible:false, //新增管理员弹窗是否可见
      tableData: [],
      total:0,
      keyWords:"",
      searchIcon:Search,
      currentPage:1,
      pageSize:10,
      level:-1,
      rules:{
        username:[{required:true,message:'请输入用户名',trigger:'blur'}],
        password:[{required:true,message:'请输入用户名',trigger:'blur'}],
        level:[{required:true,message:'请选择权限等级',trigger:'blur'}]
      }
    }
  },
  created() {
    this.init();
  },
  methods:{
    //查询用户权限
    init(){
      request.get("/admin/getLevel").then(res=>{
        if(res?.code==='0'){
          this.level=res.data
          if(this.level>2) {
            this.$message({type:'error',message:"当前用户无权查看此页面内容"})
            this.$nextTick(()=>this.$router.back())
          }
          this.load();
        } else {
          this.$message({type:'error',message:"错误："+res?.msg})
          this.$nextTick(()=>this.$router.back())
          this.$router.back();
        }
      })
    },
    //获取分页数据
    load(){
      request.get("/admin",{
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
    //新增管理员弹窗
    add(){
      this.dialogVisible=true;
      this.form={}; //清空之前的表单域
    },
    //保存修改或新增用户
    save(){
      console.log(this.form)
      this.$refs["form"].validate(valid=>{
        if(valid){
          //修改用户
          if(this.form.id){
            request.put("/admin",this.form).then(res=> {
              let options=res?.code==='0'?{type:"success",message:"编辑1成功"}:{type:"error",message:"编辑失败,错误信息:"+res?.msg}
              this.$message(options)
              this.dialogVisible = false
              this.load();
            })
          }
          //新增用户
          else {
            request.post("/admin",this.form).then(res => {
              let options=res?.code==='0'?{type:"success",message:"新增2成功"}:{type:"error",message:"新增失败,错误信息:"+res?.msg}
              this.$message(options)
              this.dialogVisible = false
              this.load();
            })
          }
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
      request.get(`/admin/info/${row.id}`).then(res=>{
        if(res?.code==='0') {
          this.form = res.data
          this.dialogVisible=true
        }
        else this.$message({type:"error",message:"操作失败，"+res?.msg})
      })
    },
    handleDelete(id){
      request.delete(`/admin/${id}`).then(res=>{
        let options=res?.code==='0'?{type:"success",message:"删除成功"}:{type:"error",message:"删除失败,错误信息:"+res.msg}
        this.$message(options)
        this.load();
      })
    }
  }
}
</script>

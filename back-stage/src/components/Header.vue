<template>
  <div class="container">
    <div class="navLeft">后台管理</div>
    <div style="flex:1"></div>
    <div class="navRight">
      <el-dropdown>
        <span class="userDropdown">
          {{nickName}}<arrow-down style="height: 15px;width: 15px;padding-left: 5px"/>
        </span>
        <template #dropdown>
          <el-dropdown-meum>
            <el-dropdown-item @click="exit">退出登录</el-dropdown-item>
          </el-dropdown-meum>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {ArrowDown} from '@element-plus/icons-vue'

export default {
  name: "Header",
  components: {ArrowDown},
  data(){
    return {
      nickName:"",
    }
  },
  methods:{
    load(){
      //session中取出user信息
      let user=JSON.parse(sessionStorage.getItem("user"))
      if(!!user) {
        this.nickName = `【lv${user.level}】管理员${user.id}`
      }
      else {
        this.nickName = '访客'
        this.$router.push("/login")
      }
    },
    exit(){
      sessionStorage.clear();
      this.$router.push("/login")
    }
  },
  created() {
    this.load();
  }
}
</script>

<style scoped>
.container{
  height: 50px; line-height: 50px;border-bottom: 1px solid #cccccc; display: flex;width: 100%;
}
.navLeft{
  width: 200px;padding-left: 30px;font-weight: bold;color: dodgerblue
}
.navRight{
  padding-right: 30px
}
.userDropdown {
  display: flex;
  align-items: center;
  height: 50px;
}
</style>
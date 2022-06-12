<template>
  <el-input
      v-model="input"
      :placeholder="placeholder"
      clearable
      @keyup.enter.native="search"
  >
    <template #prepend>
      <el-select v-model="currentSelect" style="width: 110px" @change="handleSelectChange">
        <el-option label="标题搜索" :value="1" />
        <el-option label="昵称搜索" :value="2" />
      </el-select>
    </template>
    <template #append>
      <el-icon style="cursor: pointer" @click="search"><Search/></el-icon>
    </template>
  </el-input>
</template>

<script>
const pattern=['请输入标题关建字进行搜索','请输入作者昵称进行搜索'];

export default {
  name: "SearchBar",
  data(){
    return {
      input:'',
      placeholder:pattern[0],
      currentSelect:1,
      selectText:'',
    }
  },
  methods:{
    handleSelectChange(val){
      this.placeholder=pattern[val-1]
    },
    search(){
      //将数据发给父组件进行处理
      this.$emit('handleSearch',{
        searchPattern: this.currentSelect===1?"title":"author",
        keyWords: this.input
      })
    }
  }
}
</script>

<style scoped>

</style>
<!--主体框架-->
<template>
  <div style="width: 100%;height: 100%">
    <Header/>
    <div style="display: flex;padding: 0 200px;min-height: calc( 100vh - 50px )">
      <router-view style="flex: 1"/>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header";
export default {
  name: "Layout",
  components: {Header}
}
</script>

<style scoped>

</style>
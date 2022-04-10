import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn' /*设置默认中文*/
import * as ELIcons from "@element-plus/icons-vue"
import 'element-plus/dist/index.css'
import "@/assets/css/global.css" /*全局样式*/

const app=createApp(App);

for(let iconName in ELIcons) app.component(iconName,ELIcons[iconName])
app.use(store).use(router).use(ElementPlus,{locale: zhCn});
app.mount('#app')

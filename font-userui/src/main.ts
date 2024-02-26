import { createApp } from 'vue';
import App from './App.vue';
import router from "./router";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//背景
import Particles from "vue3-particles";
//引入大菠萝
import { createPinia } from 'pinia';
//pinia持久化
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
//引入菜单
import '@imengyu/vue3-context-menu/lib/vue3-context-menu.css'
import ContextMenu from '@imengyu/vue3-context-menu'
import 'element-plus/dist/index.css'
import ElTableInfiniteScroll from "el-table-infinite-scroll";

const  app = createApp(App);
app.use(router);
app.use(ElementPlus);
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(Particles);
app.use(ContextMenu);
app.use(ElTableInfiniteScroll);

app.mount('#app');

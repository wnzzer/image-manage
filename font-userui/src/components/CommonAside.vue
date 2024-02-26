<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      :collapse="useHeaderStore().tab.isCollapse"
  >
    <img class="logo" src="../..//src/assets/云盘上传.png">
    <h1 class="title">{{useHeaderStore().tab.isCollapse? "图床":"好玩图床"}}</h1>
    <el-sub-menu v-for="item of currentMenu.items" :index="item.title">
      <template #title>
        <el-icon >
          <component :is=item.icon></component>
          </el-icon>
        <span>{{item.title}}</span>
      </template>

      <el-menu-item @click="clickMenu(cItem.path,cItem.title)" v-for="cItem of item.children" :index="cItem.title "><el-icon><component :is = cItem.icon></component></el-icon>{{cItem.title}}</el-menu-item>
    </el-sub-menu>

  </el-menu>
</template>

<script lang="ts" setup>
import {
  HomeFilled,
  Setting,
  Document,
  Picture,
User,
Coin,
} from '@element-plus/icons-vue';
import {RouteLocationRaw, useRouter} from "vue-router";
import useHeaderStore from "@/store/module/header";
import useUserStore from '@/store/module/user';
import { isClusterApi } from '@/api/login';
const router = useRouter();
const clickMenu = (path: RouteLocationRaw,title: any) => {
  router.push(path);
  //添加tag
  const headerStore = useHeaderStore();
  const flag = headerStore.tab.tabList.findIndex(element => element.path == path);
  if(flag === -1){
    headerStore.tab.tabList.push(
        {
          path :  path,
          title : title,
          icon: undefined

        })
  }

}
let  menu = {
  "userMenu": {
  "items": [
    {
      "title": "首页",
      "path": "/Home",
      "icon": HomeFilled,
      "children" : [
        {
          "title": "首页",
          "path": "/userCenter/home",
          "icon": HomeFilled,
        },
        {
          "title": "个人资料",
          "path": "/userCenter/user",
          "icon": User,
        },
      ]
    },
    {
      "title": "图库",
      "path": "/picture",
      "icon": Picture,
      "children" : [
        {
          "title": "图片管理",
          "path": "/userCenter/picture",
          "icon": Picture,
        },
        {
          "title": "使用说明",
          "path": "/userCenter/document",
          "icon": Document,
        }
      ]
    }
    
  ]
}
}
const currentMenu = menu.userMenu;

const userStore = useUserStore();
if(userStore.user.level >= 8){
  currentMenu.items.push({
      "title": "系统设置",
      "path": "/setting",
      "icon": Setting,
      "children" : [
        {
        "title": "用户",
        "path": "/setting/users",
        "icon": User
        }
      ]
    });
    isClusterApi().then((res) => {
      if(res.statusCode === 200 && res.data.isClusterModeEnabled){
        currentMenu.items[2].children.push({
        "title": "我的集群",
        "path": "/setting/servers",
        "icon": Coin
        },)
      }
    }
    )
  }
</script>

<style lang="less">
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 100%;
  height: 100vh;
  background: #F7F7F7;
}
#h1 {
  font-family: "Your Font Name", sans-serif; /* 替换为你想要使用的字体名称 */
  text-align: center;
  margin: auto;
}
.logo{
  width: 20%;
  display: flex;
  display: inline-block;
}
.title{
  display: inline-block;
}
</style>
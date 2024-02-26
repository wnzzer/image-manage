<template>
  <div class="header-container">

    <div class="l-content">
      <el-button @click="handleMenu" :icon="Menu" size="small" />
      <!-- 面包屑 -->
      <el-breadcrumb separator="/" style="margin-left: 20px">
        <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ path: item.path }">{{ item.title }}</el-breadcrumb-item>
      </el-breadcrumb>

    </div>
    <div class="r-content">
      <el-dropdown>
        <span class="el-dropdown-link">
          <img class="user" :src="avatarSrc">
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="quit">退出</el-dropdown-item>
            <el-dropdown-item @click="toUser">个人信息</el-dropdown-item>

          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { Menu } from '@element-plus/icons-vue';
import useHeaderStore from "@/store/module/header";
import {computed} from "vue";
import { avatarSrc } from '@/compositionApi/comUser';
import useUserStore from '@/store/module/user';
import router from '@/router';

const handleMenu = () => {
  const headerStore   = useHeaderStore();
  headerStore.tab.isCollapse = !headerStore.tab.isCollapse;

};

const tags = computed(() => {
  const headerStore = useHeaderStore();
  return headerStore.tab.tabList;
});

const userStore = useUserStore();
const quit = () => {
  userStore.clearToken();
  router.push("/");
  
}
const toUser = () => {
  router.push("/userCenter/user")
}





</script>
<style lang="less" scoped>
.header-container{
  padding: 0px 20px;
  background-color: #333;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .l-content {
    display: flex;
    align-items: center;
  }

  /* 使用更具体的选择器 */
  .l-content :deep(.el-breadcrumb .el-breadcrumb__item .el-breadcrumb__inner) {
    font-weight: normal;
    color: #666;
  }

  /* 使用 !important 强制应用颜色 */
  .l-content :deep(.el-breadcrumb__item .el-breadcrumb__inner.is-link) {
    color: #666 !important;
  }

  .l-content :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
    color: #fff !important;
  }

}
.user{
  width: 40px;
  height: 40px;
  border-radius: 50%;
}


</style>@/store/module/header@/store/module/header@/compositionApi/comUser
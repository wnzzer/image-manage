<template>
  <div class="tabs">
    <el-tag
        v-for="(item, index) in tags"
        :effect="router.currentRoute.value.path === item.path ? 'dark' : 'plain'"
        :key="item.path"
        :closable="item.title !== '首页'"     
        @click="changeMenu(item)"
        @close="handleClose(item, index)"
        size="small"
    >
      {{ item.title }}
    </el-tag>
  </div>
</template>

<script lang="ts" setup>
import useHeaderStore, {TabItem} from "@/store/module/header";
import {computed} from "vue";
import {useRouter} from "vue-router";
const router = useRouter()
const headerStore  = useHeaderStore();
const tags  = computed(() =>{
   return headerStore.tab.tabList
 })
function changeMenu(item : TabItem){
  router.push(item.path);
}
function handleClose(item: { path: string; },index: number){
headerStore.closeTag(item);
const length = tags.value.length;
            // 删除之后的跳转逻辑
            
            if (item.path !== router.currentRoute.value.path) {
                return
            }
            // 表示的是删除的最后一项
            if (index === length) {
                router.push(headerStore.tab.tabList[index - 1].path)
            } else {
                router.push(headerStore.tab.tabList[index]);
            }
}

</script>

<style scoped lang="less">
.tabs {
  padding-top: 0;
  padding-bottom: 20px;;
.el-tag {
  margin-right: 15px;
  cursor: pointer;
}
}
</style>
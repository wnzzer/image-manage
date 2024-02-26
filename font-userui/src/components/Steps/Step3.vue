<template>
  <div class="demo-image">
    <div v-for="fit in fits" :key="fit.name" class="block">
      <span class="demonstration">{{ fit.name }}</span>
      <el-image :preview-src-list = "urls" :initial-index = "fit.index" style="width: 100px; height: 100px" :src="fit.url" fit="cover" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import step1 from "../../static/img/document/step1.png";
import step2 from "../../static/img/document/step2.png";
import step3 from "../../static/img/document/step3.png";
import step4 from "../../static/img/document/step4.png";
import step5 from "../../static/img/document/step5.png";

const urls =  [step1,step2,step3,step4,step5]
const fits = [
  {
    index : 0,
    name : "保存参数",
    url: step1
  },
  {
    index : 1,
    name : "下载picgo",
    url: step2
  
  },
  {
    index : 2,
    name : "安装图床插件(搜索haowan)",
    url: step3
  
  },
  {
    index : 3,
    name : "配置参数",
    url: step4
  },
  {
    index : 4,
    name : "配置成功",
    url: step5
  },

];
</script>

<style scoped>
.demo-image .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  display: inline-block;
  width: 20%;
  box-sizing: border-box;
  vertical-align: top;
}
.demo-image .block:last-child {
  border-right: none;
}
.demo-image .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}
</style>
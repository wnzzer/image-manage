<template>
    <div class="contain">
    <div style="color: ;">

  <el-alert
    :closable = false
    title="提示"
    type="info"
    :description= "description"
    show-icon
  />
        
    <br>
    </div>
    <el-tree-select
    v-model="value"
    :data="data"
    check-strictly
    :render-after-expand="false"
    label = "111"
    />
    <span style="margin-left: 10px;">
      <el-button @click = "bindNewPath" type="primary" >绑定</el-button>
    </span>
    </div>
</template>
  
<script lang="ts" setup>
  import { computed, onMounted, reactive, ref } from 'vue'
  import {getAllFolders} from '@/api/user/image'
  import { ElMessage } from 'element-plus';
import { getDirectLinkToken, putDirectLinkToken } from '@/api/document/document';
  
  const value = ref()
  let currentPath = ref("/");
  const description = computed(() => {
    return `选择图床存储目录，当前目录为:"${currentPath.value}"`;
  })
  onMounted(async () => {
    const tokenData = await getDirectLinkToken();
    if(tokenData.statusCode === 200){
      if(tokenData.data.storePath){
        currentPath.value = tokenData.data.storePath;
      }
    }
    
    const dataResponse = await getAllFolders();
    if(dataResponse.statusCode === 200){
      data[0].children = dataResponse.data.children
      
    }else{
      ElMessage.error(dataResponse.message);
    }
  })
  const data = reactive([
    {
      value: 1,
      label : "/",
      children : [] as any[]
    }
  ])
  const bindNewPath = async () => {
    const res = await putDirectLinkToken({storePath : value.value});
    if(res.statusCode === 200){
      ElMessage.success("success");
      if(res.data.storePath){
        currentPath = res.data.storePath
      }
    }else{
      ElMessage.error(res.message);
    }
  }

  
</script>
<style lang ="less" scoped>
.contain{
    margin-left: 15%;
    margin-top: 5%;
    margin-bottom: 5%;
} 
</style>
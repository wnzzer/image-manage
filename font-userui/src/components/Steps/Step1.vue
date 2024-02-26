<template>
    <div class="contain">
        <el-descriptions 
      class="margin-top"
      title="获取Token"
      v-loading="loading"
      :column="2"

      border
      style="margin-left: 10%; margin-right: 10%;"
    >
      <template #extra>
        <el-popconfirm :icon="Warning" @confirm="resetToken()" title="这会使你之前的图片直链失效，你确定要重置吗?">
            <template #reference><el-button type="danger">重置token</el-button></template>
        
       </el-popconfirm>
      </template>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <user />
            </el-icon>
            服务器域名
          </div>
        </template>
        {{  behindAddress}}
      </el-descriptions-item>
     
      <el-descriptions-item >
        <template #label>
          <div class="cell-item">
            <el-icon>
              <Key />
            </el-icon>
            Token
          </div>
        </template>
        {{ directLinkTokenInstance.linkToken }}
      </el-descriptions-item>
      
      <el-descriptions-item >
        <template #label>
          <div class="cell-item">
            <el-icon>
              <InfoFilled />
            </el-icon>
            提示
          </div>
        </template>
        token是图床上传文件的凭证，请妥善保存
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions>
        
    </el-descriptions>
    </div>
  
    
  </template>
  
  <script  lang="ts" setup>
  import { computed, onMounted, ref } from 'vue'
  import {
    Key,
    User,
    InfoFilled,
    Warning
  } from '@element-plus/icons-vue'
import { behindAddress } from '@/utils/request';
import {getDirectLinkToken,putDirectLinkToken} from '@/api/document/document';
import useLoadingStore from '@/store/module/loading'
  const behindUrl = behindAddress;
  const directLinkTokenInstance = ref({

});
const loadingStore = useLoadingStore();
const loading = computed(()=>{
  return loadingStore.loading
});
onMounted(async () =>{
  const data = await getDirectLinkToken();
  directLinkTokenInstance.value = data.data;

})
const resetToken = async () => {
  const data = await putDirectLinkToken({linkToken : "1"});
  directLinkTokenInstance.value = data.data;
}
 
  </script>
  
  <style scoped>
  .el-descriptions {
    margin-top: 20px;
  }
  .cell-item {
    display: flex;
    align-items: center;
  }
  .margin-top {
    margin-top: 20px;
  }
  .contain{
    width: 1000px;
    align-items: center;
    vertical-align: middle;
  }
  
  </style>
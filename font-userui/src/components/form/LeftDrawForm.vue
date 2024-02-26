<template>
    <span style="margin-left: 3%;"> <el-button type="primary"  @click="dialog = true" size = "large" :icon="Plus" circle /> </span>
    
    <el-drawer
      ref="drawerRef"
      v-model="dialog"
      title="新增用户"
      direction="ltr"
      class="demo-drawer" 
      @close="handleDrawerClose()"
    >
      <div class="demo-drawer__content">
        <el-form :model="form"   :rules="props.rules">
          <el-form-item label="用户名"  prop="username" :required = "true" :label-width="formLabelWidth">
            <el-input v-model="form.username" autocomplete="off" />
          </el-form-item>
          <el-form-item label="密码"  prop="password" :required = "true" :label-width="formLabelWidth">
            <el-input v-model="form.password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="权限"  :required = "true" :label-width="formLabelWidth">
            <el-select
              v-model="form.level"
              placeholder="请选择用户权限"
            >
              <el-option label="普通用户" :value="1" />
              <el-option label="管理员" :value="8" />
            </el-select>
          </el-form-item>
        </el-form>
        <div class="demo-drawer__footer">
          <el-button @click="cancelForm">返回</el-button>
          <el-button type="primary" :loading="loading" @click="onClick">{{
            loading ? '提交中 ...' : '提交'
          }}</el-button>
        </div>
      </div>
    </el-drawer>
  </template>
  
  <script lang="ts" setup>
  import { reactive, ref } from 'vue'
  import {Plus} from '@element-plus/icons-vue'
  import { addUser } from '@/api/setting/user/user';
  import { ElMessage } from 'element-plus';
  import useLoadingStore from '@/store/module/loading';
import { User } from '@/api/setting/user/type';
  
  const formLabelWidth = '80px'
    const dialog = ref(false)
  const useLoading = useLoadingStore();
  const loading = useLoading.loading;
  
  const cancelForm = () => {
    dialog.value = false;
  }
  const form = reactive({
    id : 1,
    username: '',
    password : '',
    level : 1

  })
  const handleDrawerClose = () => {
    form.id = 1,
    form.username = '',
    form.password = '',
    form.level = 1
  }

  const props = defineProps<{
  rules: any,
  user : User
}>();

  const emit = defineEmits();
  const onClick = async () => {
    const res = await addUser(form);
    if(res.statusCode === 200){
      ElMessage.success(res.data);
      dialog.value = false;
      emit("flush");
    }
    
    
    
  }

  </script>
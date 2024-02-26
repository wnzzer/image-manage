<template>
  <el-drawer ref="drawerRef" v-model="dialog" title="编辑" @close="handleDrawerClose()"  class="demo-drawer">
    <div class="demo-drawer__content">
      <el-form :model="form" :rules="props.rules">
        <el-form-item label="用户名"  :label-width="formLabelWidth">
          <el-input v-model="form.username" :disabled="true" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password" :required="true" :label-width="formLabelWidth">
          <el-input v-model="form.password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="权限" prop="level" :required="true" :label-width="formLabelWidth">
          <el-select v-model="form.level" placeholder="请选择用户权限">
            <el-option label="普通用户" :value="1" />
            <el-option label="管理员"  :disabled="props.user.level<=8" :value="8" />
            <el-option label="超级管理员" :disabled="props.user.level<=9" :value="9" />


          </el-select>
        </el-form-item>
      </el-form>
      <div class="demo-drawer__footer">
        <el-button @click="cancelForm">返回</el-button>
        <el-button type="primary" :loading="loading" @click="onClick()">{{
          loading ? '提交中 ...' : '提交'
        }}</el-button>
      </div>
    </div>
  </el-drawer>
</template>
  
<script lang="ts" setup>
import { User } from '@/api/setting/user/type';
import { putUser } from '@/api/setting/user/user';
import { ElMessage } from 'element-plus';
import { reactive, ref } from 'vue'
const formLabelWidth = '80px'
const dialog = ref(false)
const loading = ref(false)

const appearForm = (user:User) => {
  if(props.user.username === user.username){
    ElMessage.warning("不支持修改自己(请在首页修改)")
    return;
  }
  dialog.value = true;
  console.log(user)
  if(user.id && user.level && user.username){
    
    form.username = user.username;
    form.level = user.level;
    form.id = user.id;
  }

}

const cancelForm = () => {
  dialog.value = false;
}
defineExpose({
  appearForm

})
const form = reactive({
  id : 0,
  username: '',
  level: 1,
  password: ''

})

const onClick = async () => {
  const res = await putUser(form);
  if(res.statusCode === 200){
    ElMessage.success(res.data);
  }
  
}
const handleDrawerClose = () => {
  form.id = 0;
  form.username = '';
  form.level = 1;
  form.password = ''
}

const props = defineProps<{
  rules: any,
  user : any
}>();

</script>
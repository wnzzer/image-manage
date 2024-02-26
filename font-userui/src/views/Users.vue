<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <span><el-text class="mx-1" size="large">用户</el-text></span>
        <LeftDrawForm :rules="rules" :user="currentUser" @flush="flushUsers()"/>
        <RightDrawForm :rules="rules" :user="currentUser" ref="rightFormRef"/>

      </el-header>
      <el-main>
        <el-scrollbar height="100vh">
          <div style="display: flex;flex-wrap: wrap;
  justify-content: start; /* 或者 'space-between' 根据您的布局需求 */
  align-items: stretch;">
            <!-- 显示骨架屏当用户数据正在加载 -->
            <el-skeleton v-for="n in 12" :key="n" v-if="isLoading" animated
              style="width: 250px; height: 160px; margin: 20px;">
              <!-- 模拟头像和标题 -->
              <el-skeleton-item variant="avatar" size="40px" style="margin-left: 20px;"></el-skeleton-item>
              <el-skeleton-item variant="text" style="width: 70%; margin-left: 20px;"></el-skeleton-item>

              <!-- 模拟主要内容 -->
              <el-skeleton-item variant="text" style="margin: 10px 20px;"></el-skeleton-item>
              <el-skeleton-item variant="text" style="margin: 10px 20px; width: 80%;"></el-skeleton-item>

              <!-- 模拟底部时间信息 -->
              <el-skeleton-item variant="text" style="margin-left: 110px; width: 50%;"></el-skeleton-item>
            </el-skeleton>

            <!-- 显示用户卡片当数据加载完成 -->
            <div v-else>
              <UserCard v-for="user in users" :key="user.id" :img="transAvatar(user)" @onCardEdit="appearEdit(user)"
                @onDeleteButton="toDelete(user)">
                {{ user.username }}
                <template #level>
                  {{ returnLevelName(user) }}
                </template>
                <template #lastLoginTime>
                  {{ `最后一次登陆${DateTimeConverter.toCustomDayFormat(user.lastLoginTime)} ` }}
                </template>
              </UserCard>
            </div>
          </div>

        </el-scrollbar>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts" setup>
import UserCard from '@/components/Setting/UserCard.vue'
import LeftDrawForm from '@/components/form/LeftDrawForm.vue'
import RightDrawForm from '@/components/form/RightDrawForm.vue'
import { deleteUser, getUsers } from '@/api/setting/user/user'
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { SimplyUser, User } from '@/api/setting/user/type';
import { DateTimeConverter } from '@/utils/time'
import useUserStore from '@/store/module/user';
import useLoadingStore from '@/store/module/loading';
import meiyangyang from "@/assets/meiyangyang.jpg"
const users = ref([] as SimplyUser[]);

onMounted(() => {
  getUsersApi();
})

const getUsersApi = async () => {
  const data = await getUsers();
  if (data.statusCode === 200) {
    users.value = data.data.filter(user => user.level < currentUser.level || user.username === currentUser.username);
  } else {
    ElMessage.error(data.message);
  }

}
const returnLevelName = (user: User) => {
  let levelName = "";
  if (user.level) {
    if (user.level <= 1) {
      levelName = "普通用户";
    } else if (user.level === 8) {
      levelName = "管理员";
    } else if (user.level === 9) {
      levelName = "超级管理员";
    } else {
      levelName = "未知权限";
    }
    if (userStore.user.username === user.username) {
      levelName += "(本用户)"
    }
    return levelName;
  }
}
const userStore = useUserStore();
const loading = useLoadingStore();
const isLoading = loading.loading;
const currentUser = userStore.user;
const rightFormRef = ref();
const appearEdit = (user: User) => {
  // 确保 rightForm 存在并且有 appearEdit 方法

  rightFormRef.value.appearForm(user);

}
const toDelete = async (user: User) => {
  if (user.id) {
    const res = await deleteUser(user.id);
    if (res.statusCode === 200) {
      ElMessage.success(res.data);
      flushUsers();
    }
  }
}

const flushUsers = () => {
  console.log("执行了flush")
  getUsersApi();
}



const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 15, message: '需要3到15和字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 20, message: '需要8到20和字符', trigger: 'blur' },
  ],
  level: {

  }

}


const transAvatar = (user: SimplyUser) => {
  if(user && user.thumbnailAvatar){
    return `data:image/png;base64,${user.thumbnailAvatar}`;
  }else{
    return meiyangyang;
  }

}
</script>
<style scope>
.example-showcase .el-dropdown+.el-dropdown {
  margin-left: 15px;
}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>
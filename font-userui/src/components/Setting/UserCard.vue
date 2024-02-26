<template>
  <div class="card" style="weight : 30%">
    <el-card class="box-card">
      <template #header>
        <div class="card-header" >

          <span><el-avatar shape="square"  :size="40" :src="props.img" /></span>
          <slot></slot>
          <el-dropdown>
            <span>
              <el-icon :size="30" >
                <ArrowDown />
              </el-icon>
            </span>
            <span>
              &ensp;&ensp;
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="onEdit()">编辑</el-dropdown-item>
                <el-popconfirm title="确定要删除吗?" @confirm="onDelete()">
                  <template #reference>
                    <el-button>删除</el-button>
                  </template>
                </el-popconfirm>

              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </template>
      <el-icon>
        <User />
      </el-icon>
      <span>
        <slot name="level"></slot>
      </span>
      <div class="time"><el-icon>
          <InfoFilled />
        </el-icon>
        <slot name="lastLoginTime"></slot>
      </div>
    </el-card>
  </div>
</template>
<script lang="ts" setup>
import { InfoFilled, ArrowDown, User} from '@element-plus/icons-vue'
import { defineEmits } from 'vue';


// 定义 emit
const emit = defineEmits();
const onEdit = () => {
  emit("onCardEdit");
}

const onDelete = () => {
  emit("onDeleteButton");
}

const props = defineProps({
  img : null
})

</script>

<style lang="less">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 250px;
  height: 160px;
  margin: 20px;
}

.card {
  display: inline-block;
}

.moreButton:hover {
  cursor: pointer;
}

.time {
  font-size: 13px;
  margin-left: 110px;
  color: #bfc5cc;
}
</style>
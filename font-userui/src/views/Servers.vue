<template>
    <div>
        <el-container>
            <el-header>
                <div class="header">
                    <el-tabs v-model="activeName" type="card" class="demo-tabs" @tab-click="handleClick">
                        <el-tab-pane label="元数据" name="first"></el-tab-pane>
                        <el-tab-pane label="运行时" name="second"></el-tab-pane>
                    </el-tabs>
                </div>
            </el-header>
            <el-main>
                <el-scrollbar height="100vh">
                    <component :is="getComponent()" />
                </el-scrollbar>

            </el-main>
        </el-container>
    </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';
import MetaData from '@/components/Setting/MetaData.vue'
import RunTime from '@/components/Setting/RunTime.vue'


const activeName = ref('first')

const handleClick = (tab: any) => {
    console.log(tab)
}
const getComponent = () => {
    if (activeName.value === 'first') {
        return MetaData;
    } else {
        return RunTime;
    }
}
</script>
<style lang="less" scoped>
.header {
    width: 100%;

}
</style>
<template>
    <div>
        <el-container>
            <el-main>

                <el-card ref="one" v-loading="loadingState.loading" class="box-card">
                    <template #header>
                        <div class="card-header">
                            <span style="font-size: small;">基本信息</span>
                            <el-button @click="openYaml" type="primary" plain>statefulSet yaml</el-button>
                            <el-dialog ref="dialog" v-model="visible" :show-close="false" width="800">
                                <template #header="{ close, titleId, titleClass }">
                                    <div class="my-header" >
                                        <h4 :id="titleId" :class="titleClass">statefulSet yaml</h4>
                                        <el-button type="danger" size="small" @click="close">
                                            <el-icon class="el-icon--left">
                                                <CircleCloseFilled/>
                                            </el-icon>
                                            离开
                                        </el-button>
                                        <el-button type="primary"  size="small" @click="close">
                                            <el-icon @click="copyYaml()" class="el-icon--left">
                                                <Document/>
                                            </el-icon>
                                            复制
                                        </el-button>
                                        
                                    </div>
                                </template>
                                <pre style="width: 750;"><code  class="yml" style="width: 750;" >{{trimYaml}}</code></pre>
                            </el-dialog>
                        </div>
                    </template>
                    <div class="card-main">
                        <p><span class="des-name">工作负载名称</span><span class="des-value">
                                {{ podSimplyInformation.workName }}</span> </p>
                        <p><span class="des-name">工作负载类型</span>{{ podSimplyInformation.workType }}<span
                                class="des-value"></span>
                        </p>
                        <p><span class="des-name">工作负载描述</span><span class="des-value"></span> </p>

                        <p><span class="des-name">标签</span><span class="des-value">
                                <el-tag v-for="i of podSimplyInformation.labels" :key="i" effect="dark" round>
                                    {{ i }}
                                </el-tag>
                            </span> </p>
                        <p><span class="des-name">选择器</span><span class="des-value">
                                <el-tag v-for="i of podSimplyInformation.selector" :key="i" effect="dark" round>
                                    {{ i }}
                                </el-tag>
                            </span> </p>

                    </div>
                </el-card>


            </el-main>
            <el-footer>
                <el-card ref="two" class="foot-card">
                    <template #header>
                        <div class="card-header">
                            <span style="font-size: small;">运行时信息</span>
                          


                        </div>
                    </template>
                    <div class="text item"><el-table :data="tableData" style="width: 100%">
                            <el-table-column fixed prop="type" label="Type" width="100" />
                            <el-table-column prop="status" label="Status" width="120" />
                            <el-table-column prop="message" label="message" width="500" />
                            <el-table-column prop="reason" label="reason" width="120" />
                            <el-table-column prop="lastUpdateTime" label="lastUpdate Time" width="200" />

                        </el-table></div>
                </el-card>
            </el-footer>
        </el-container>
    </div>
</template>
<script setup lang="ts">
import { Ref, computed, nextTick, onMounted, ref } from 'vue';
import { PodRunTimeItem,  PodSimplyInformation } from '@/api/setting/metadata/type';
import { getMetaDataApi } from '@/api/setting/metadata/metadata'
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css'; // 引入样式文件
import {Document,CircleCloseFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import useLoadingStore from '@/store/module/loading';


const loadingState = useLoadingStore();

const dialog = ref();
onMounted(() => {
    getMetaData();
})

const openYaml = () => {
    visible.value = true;
    nextTick(() => {
        document.querySelectorAll('pre code').forEach((block) => {
            hljs.highlightElement(block as HTMLElement);
        });
    });

}

const visible = ref(false);

const getMetaData = async () => {
    const res = await getMetaDataApi();
    if (res.statusCode === 200) {
        if(res.data.podRunTimeTable){
            tableData.value = res.data.podRunTimeTable;
        }
        podSimplyInformation.value = res.data.podSimplyInformation;
        yaml.value = res.data.yaml;
    }
}



const yaml = ref();
const trimYaml  =  computed (() => {
    return yaml.value.trim();
})

const copyYaml = async () => {
    try {
        await navigator.clipboard.writeText(trimYaml.value);
        ElMessage.success('复制成功！');
      } catch (err) {
        ElMessage.error('复制失败：'+ err);
      }
}
const podSimplyInformation: Ref<PodSimplyInformation> = ref({
    workName: "image-manage  behind",
    workType: "StatefulSet",
    annotations: ["k8s.kuboard.cn/layer web"],
    labels: ["k8s.kuboard.cn/layer web"],
    selector: ["k8s.kuboard.cn/layer web"]
});
const tableData: Ref<PodRunTimeItem[]> = ref(
    [
        {
            type: '',
            status: '',
            message: '',
            reason: '',
            lastUpdateTime: ''
        }
    ]
);


</script>
<style lang="less" scoped>
.box-card {
    margin: 0;
    width: 99%;
    min-height: 50vh;
    border-radius: 10px;
    /* 设置圆角的大小 */

}

.foot-card {
    width: 99%;
    border-radius: 10px;
    /* 设置圆角的大小 */
}

.card-header {
    display: flex;
    justify-content: space-between;
    /* 会在两个元素之间提供最大的空间，推动它们分别到容器的两端 */
}

.des-name {
    margin-right: 50px;
    color: #646464;
    font-size: 14px;
}
</style>
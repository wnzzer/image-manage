<template>
    <div>
        <el-container>
            <el-header>
                <div class="container-header">
                    <div style="margin-right: 20px; font-size: 14px color: #2c3e50;">
                        <span>
                            <el-icon>
                                <Switch />
                            </el-icon>
                        </span>
                        <span>
                            伸缩:
                        </span>
                    </div>

                    <div style="width: 300px;">
                        <el-slider @change="changePods" v-model="setPods" :min="1" show-input />
                    </div>
                    <div style="font-size: 12px; margin-left: 10px;">
                        当前副本数
                        <span class="value">
                            {{ podStatus?.totalPods }}
                        </span>
                    </div>
                </div>
            </el-header>
            <el-main>
                <el-container>
                    <el-aside width="50%">
                        <div style="width: 100%; height: 50px; margin-bottom: 10px; ">
                            <div>

                                <div
                                    style="width: 100px; padding-top: 10px; display: inline-block; background-color: #234883;color: white;font-size: small;">

                                    <div class="circle" />就绪/创建
                                    <div style="text-align: center; padding-bottom: 5px;">
                                        {{ podStatus ? (podStatus.totalPods - podStatus.creatingPods) : '0' }}/{{
                                            podStatus?.totalPods }}
                                    </div>
                                </div>

                                <div style="display: inline-block; width: 250px; margin-left: 5px;">
                                    <span class="value">image-manage </span>所管理的容器组
                                </div>
                            </div>
                        </div>
                        <div class="left-card">
                            <div style="margin: 5px;">
                                <el-card class="box-card">
                                    <template #header>
                                        <div>
                                            <span>
                                                <el-icon>
                                                    <Monitor />
                                                </el-icon>
                                            </span>
                                            我的网络:
                                        </div>
                                    </template>
                                    <div style="width: 90%;">
                                        <div class="item">
                                            <span>
                                                <el-icon>
                                                    <Location />
                                                </el-icon>
                                            </span>
                                            <span>
                                                集群ip:
                                            </span>
                                            <span class="value">
                                                {{ serviceInfo?.clusterIp }}
                                            </span>


                                        </div>
                                        <div>
                                            <span>
                                                <el-icon>
                                                    <Aim />
                                                </el-icon>
                                            </span>

                                            <span class="item">
                                                集群端口:
                                            </span>
                                            <span class="value">
                                                {{ serviceInfo?.port }}
                                            </span>
                                        </div>


                                    </div>
                                </el-card>
                                <div style="margin-top: 10px;">
                                    <el-card>
                                        <template #header>
                                            <div>
                                                <span>
                                                    <span>
                                                        <el-icon>
                                                            <Camera />
                                                        </el-icon>
                                                    </span>
                                                    资源监控
                                                </span>
                                                <el-select v-model="selectPod" class="m-2" placeholder="Select" size="large"
                                                    style="width: 240px">
                                                    <el-option v-for="item in allPods" :key="item" :label="item"
                                                        :value="item" />
                                                </el-select>
                                            </div>
                                        </template>

                                        <div style="display: flex; justify-content: space-between; margin-top: 10px;">

                                            <el-card id="chart1" style="width: 49%; min-height: 200px;">

                                            </el-card>
                                            <el-card id="chart2" style=" width: 49%; min-height: 200px">

                                            </el-card>

                                        </div>
                                    </el-card>
                                </div>
                            </div>

                        </div>
                    </el-aside>
                    <el-main>
                        <div style="width: 100%">

                            <div>
                                <el-card class="box-card">
                                    <template #header>
                                        <div>
                                            <span>
                                                <el-icon>
                                                    <Grid />
                                                </el-icon>
                                            </span>
                                            我的节点:
                                        </div>
                                    </template>
                                    <div style="width: 99%;">

                                        <div class="item">
                                            <div class="white-circle" />
                                            <span>
                                                在创建:
                                            </span>
                                            <span class="podValue">
                                                <span class="value">
                                                    {{ podStatus?.creatingPods }}
                                                </span>
                                                <span>
                                                    个副本
                                                </span>
                                            </span>

                                        </div>
                                        <div class="item">
                                            <div class="purple-circle" />

                                            <span>
                                                已就绪:
                                            </span>
                                            <span class="podValue">
                                                <span class="value">
                                                    {{ podStatus?.readyPods }}
                                                </span>
                                                <span>
                                                    个副本
                                                </span>
                                            </span>
                                        </div>
                                        <div class="item">
                                            <div class="blue-circle" />

                                            <span>
                                                同步中:
                                            </span>
                                            <span class="podValue">
                                                <span class="value">
                                                    {{ podStatus?.syncingPods }}
                                                </span>
                                                <span>
                                                    个副本
                                                </span>
                                            </span>
                                        </div>
                                        <div class="item">
                                            <div class="green-circle" />

                                            <span>
                                                已同步:
                                            </span>
                                            <span class="podValue">
                                                <span class="value">
                                                    {{ podStatus?.syncedPods }}
                                                </span>
                                                <span>
                                                    个副本
                                                </span>
                                            </span>
                                        </div>


                                    </div>
                                </el-card>
                            </div>
                            <div style="width: 90%; margin-top: 5px;">
                                <el-card>
                                    <template #header>

                                        <div>
                                            <span><el-icon>
                                                    <Tickets />
                                                </el-icon></span>

                                            <span> 我的日志</span>
                                            <span style="margin-left: 10px;">
                                                <el-select v-model="comResource" class="m-2" placeholder="Select"
                                                    size="large" style="width: 240px">
                                                    <el-option v-for="item in allPods" :key="item" :label="item"
                                                        :value="item" />
                                                </el-select>
                                            </span>
                                        </div>
                                    </template>
                                    <div style="min-height: 200px; max-height: 400px;" ref="divTable">

                                        <el-table v-el-table-infinite-scroll="getMoreLog" :data="filterLogs"
                                            :row-class-name="tableRowClassName" height="280"
                                            style="min-height: 200px; max-height: 400px;">
                                            <el-table-column prop="levelString" label="level" width="70" />

                                            <el-table-column prop="message" label="message" width="300" />
                                            <el-table-column prop="date" label="date" width="80" />
                                            <el-table-column prop="podName" label="podName" />
                                        </el-table>
                                    </div>
                                </el-card>
                            </div>

                        </div>
                    </el-main>
                </el-container>

            </el-main>
        </el-container>
    </div>
</template>
<script setup lang="ts">
import { computed, onMounted, reactive, ref, watchEffect } from 'vue';
import { Switch, Monitor, Grid, Location, Aim, Tickets, Camera } from '@element-plus/icons-vue'
import { getClusterServiceApi, getPodLogsApi, getPodMetricsApi, getPodStatusApi, putReplicasApi } from '@/api/setting/runtime/runtime';
import { DateTimeConverter } from '@/utils/time';
import useLoadingStore from '@/store/module/loading';
import * as echarts from 'echarts';
import { ElMessage, ElMessageBox } from 'element-plus';

const allPods = reactive<string[]>([]);

const comResource = computed(() => {
    if (filterText.value) {
        return filterText.value;
    } else {
        return "all";
    }
})

const divTable = ref();
onMounted(async () => {
    await getPodStatus();

    getLogs(0);
    getMetrics();
    getClusterService();
});
//滑块
const changePods = (value: any) => {
    console.log(value)
    ElMessageBox.confirm('您确定要伸缩吗？', '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        // 用户点击了“确定”，标记为已确认更改
        const res = await putReplicasApi(value);
        if (res.statusCode === 200) {
            ElMessage.success("更新配置成功，等待节点改变")
            getPodStatus();
            getMetrics();
        }
    }).catch(() => {
        // 用户点击了“取消”，还原值并阻止再次触发
        if (podStatus.value) {
            setPods.value = podStatus.value.totalPods;
        }

    });
}
// 日志相关
type LogTableItem = {
    levelString: string,
    message: string,
    date: string,
    podName: string
}

var currentLogPage = ref(0);

const logs = reactive<LogTableItem[]>([]);
const getLogs = async (page: number) => {

    const res = await getPodLogsApi(page);
    if (res.statusCode === 200) {
        for (const log of res.data) {
            logs.push({
                levelString: log.levelString,
                message: log.callerFilename + ":" + log.callerClass + ":" + log.callerMethod + ":" + log.formattedMessage,
                date: DateTimeConverter.toCustomMinuteFormat(log.timestamp),
                podName: log.podName
            });


        }



    }

}
const getMoreLog = () => {
    const loadingStore = useLoadingStore();
    if (loadingStore.loading) {
        return;
    }
    getLogs(currentLogPage.value + 1);

}



const filterText = ref("");
const filterLogs = computed(() => {
    return logs.filter((log) => {
        return log.podName && log.podName.includes(filterText.value); // Safely use .includes()
    });
});


const tableRowClassName = ({
    row }: {
        row: LoggingEvent
    }) => {
    return row.levelString.toLowerCase() + '-row';
}
const setPods = ref(1);


//pod状态相关
const podStatus = ref<PodsStatus>();
const getPodStatus = async () => {
    const res = await getPodStatusApi();
    if (res.statusCode === 200) {
        if (res.data) {
            podStatus.value = res.data;
            setPods.value = podStatus.value.totalPods;
            allPods.push(...res.data.syncedPodNames, ...res.data.syncingPodNames);

        }
    }
}

//资源图相关
const selectPod = ref<string>()
const metricsArr: ResponsePodMetrics[] = [];

const getMetrics = async () => {
    const res = await getPodMetricsApi();
    if (res.statusCode === 200) {
        const resData = res.data;
        if (resData) {
            for (const temMetrics of resData) {
                metricsArr.push(temMetrics);
            }


            const optionArr = getOption("", metricsArr)
            const myChart1 = echarts.init(document.getElementById('chart1'));
            myChart1.setOption(optionArr[0]);
            const myChart2 = echarts.init(document.getElementById('chart2'));
            myChart2.setOption(optionArr[1]);
            watchEffect(() => {
                if (selectPod && selectPod.value) {
                    const optionArr = getOption(selectPod.value, metricsArr);
                    const myChart1 = echarts.init(document.getElementById('chart1'));
                    myChart1.setOption(optionArr[0]);
                    const myChart2 = echarts.init(document.getElementById('chart2'));
                    myChart2.setOption(optionArr[1]);
                }

            })

        }
    }
}

const comX = () => {
    const stringTime: string[] = [];
    const timeArr: Date[] = [];
    const now = new Date();

    // 循环30次，生成过去30分钟的时间字符串
    for (let i = 1; i <= 29; i++) {
        // 创建一个新的日期对象，减去i分钟
        const time = new Date(now.getTime() - i * 60000); // 60000毫秒等于1分钟
        let hours = time.getHours(); // 获取当前小时 (0-23)
        let minutes = time.getMinutes(); // 获取当前分钟 (0-59) 
        // 格式化时间：确保小时和分钟都是两位数
        // 如果小时或分钟小于10，则在前面添加一个'0'
        const hourString = hours < 10 ? '0' + hours : hours;
        const minuteString = minutes < 10 ? '0' + minutes : minutes;

        // 构建表示小时和分钟的字符串
        let timeString = hourString + ":" + minuteString;
        stringTime.unshift(timeString); // 使用ISO字符串，截取小时和分钟部分
        timeArr.unshift(time);
    }

    return [stringTime, timeArr];
}

function getOption(podName: string, metricsArr: ResponsePodMetrics[]) {
    let name = allPods[0];
    if (podName) {
        name = podName;
    }
    const cpuArr = [];
    const memoryArr = [];
    const timeArr = comX();


    const first = metricsArr.find(metrics => metrics.podName === name);

    for (const date of timeArr[1]) {
        let flag = 0;
        for (const str of first.cpuUsage) {
            if (areDatesInSameMinute(new Date((parseInt(str.split(":")[0])) * 1000), date)) {
                cpuArr.push(convertCpuToCore(str.split(":")[1]));
                flag = 1;
                break;
            }
        }
        if (flag === 0) {
            cpuArr.push(0)
        }



    }
    const option1 = JSON.parse(JSON.stringify(option));;
    option1.title.text = "cpu使用(cores)"
    option1.series = { name: "cpu使用", type: 'line', data: cpuArr };
    option1.yAxis = {

        type: 'value',
        axisLabel: {
            formatter: '{value} m'
        },
    };
    option1.xAxis = {
        axisLabel: {
            interval: 6, // 表示间隔几个标签显示一次。这里的2表示每隔2个标签显示一个标签
        },
        type: 'category',
        data: timeArr[0],
    }


    for (const date of timeArr[1]) {
        let flag = 0;
        for (const str of first.memoryUsage) {
            debugger;

            if (areDatesInSameMinute(new Date(parseInt(str.split(":")[0]) * 1000), date)) {
                memoryArr.push(convertMemoryToMi(str.split(":")[1]));
                flag = 1;
                break;
            }
        }
        if (flag === 0) memoryArr.push(0);

    }
    const option2 = JSON.parse(JSON.stringify(option));
    option2.title.text = "内存使用(Mib)";
    option2.series = { name: "内存使用(Mib )", type: 'line', data: memoryArr };
    option2.yAxis = {
        type: 'value',
        axisLabel: {
            formatter: '{value} Mib'
        }
    };


    option2.xAxis = {
        type: 'category',
        data: timeArr[0],
        axisLabel: {
            interval: 6, // 表示间隔几个标签显示一次。这里的2表示每隔2个标签显示一个标签
        },
    }
    return [option1, option2];

}
function areDatesInSameMinute(date1: Date, date2: Date) {
    return date1.getFullYear() === date2.getFullYear() && // 比较年份
        date1.getMonth() === date2.getMonth() && // 比较月份
        date1.getDate() === date2.getDate() && // 比较日期
        date1.getHours() === date2.getHours() && // 比较小时
        date1.getMinutes() === date2.getMinutes(); // 比较分钟
}
type Unit = 'Ki' | 'Mi' | 'Gi' | 'Ti';

function convertMemoryToMi(memoryStr: string) {
    const unitMultipliers = {
        Ki: 1 / 1024,
        Mi: 1,
        Gi: 1024,
        Ti: 1024 * 1024
    };

    if (!memoryStr) return 0; // 或者抛出一个错误

    const match = memoryStr.match(/(\d+(?:\.\d+)?)(Ki|Mi|Gi|Ti)?/);
    if (!match) return 0; // 或者抛出一个错误

    const value = parseFloat(match[1]);
    const unit = match[2] || 'MiB'; // 默认单位为MiB
    const multiplier = unitMultipliers[unit as Unit];

    return (value * multiplier).toFixed(2); // 转换为MiB并保留两位小数
}
function convertCpuToCore(cpuStr: string) {
    if (!cpuStr) return 0; // 或者抛出一个错误

    const match = cpuStr.match(/(\d+)(m)?/);
    if (!match) return 0; // 或者抛出一个错误

    const value = parseInt(match[1], 10);

    return value? value : 0;
}

//折线图数据
const option = {

    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis'
    },

    grid: {
        left: '3%',
        right: '4%',
        bottom: '10%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: '',
        axisLabel: {
            interval: 10,
            rotate: 45
        }
    },
    yAxis: {
        type: 'value'
    },
    series: {

    }
};
//服务基本信息相关
const serviceInfo = ref<{ clusterIp: string, port: string }>();
const getClusterService = async () => {
    const res = await getClusterServiceApi();
    if (res.statusCode === 200) {
        serviceInfo.value = res.data;
    }
} 
</script>
<style lang="less" scoped>
.container-header {
    display: flex;
    align-items: center;
    /* 垂直居中 */
    ;
    padding-bottom: 20px;
    /* 添加内边距使内容不紧贴边框 */
    margin-bottom: 20px;
    /* 添加外边距确保盒子间有间隔 */
    border-bottom: 1px dashed #aaa;
    /* 使用较淡的颜色和更宽的虚线 */
}

.circle {
    margin-right: 5px;
    display: inline-block;
    width: 10px;
    /* 盒子的宽度 */
    height: 10px;
    /* 盒子的高度 */
    background-color: #0fbe8f;
    /* 盒子的背景颜色 */
    border-radius: 50%;
    /* 圆角的半径，50% 会创建一个完美的圆形 */
}

.white-circle {
    margin-right: 5px;
    display: inline-block;
    width: 10px;
    /* 盒子的宽度 */
    height: 10px;
    /* 盒子的高度 */
    background-color: #F1F4FA;
    /* 盒子的背景颜色 */
    border-radius: 50%;
    /* 圆角的半径，50% 会创建一个完美的圆形 */
}

.purple-circle {
    margin-right: 5px;
    display: inline-block;
    width: 10px;
    /* 盒子的宽度 */
    height: 10px;
    /* 盒子的高度 */
    background-color: #7700ff;
    /* 盒子的背景颜色 */
    border-radius: 50%;
    /* 圆角的半径，50% 会创建一个完美的圆形 */
}

.blue-circle {
    margin-right: 5px;
    display: inline-block;
    width: 10px;
    /* 盒子的宽度 */
    height: 10px;
    /* 盒子的高度 */
    background-color: #0442fd;
    /* 盒子的背景颜色 */
    border-radius: 50%;
    /* 圆角的半径，50% 会创建一个完美的圆形 */
}

.green-circle {
    margin-right: 5px;
    display: inline-block;
    width: 10px;
    /* 盒子的宽度 */
    height: 10px;
    /* 盒子的高度 */
    background-color: #0fbe8f;
    /* 盒子的背景颜色 */
    border-radius: 50%;
    /* 圆角的半径，50% 会创建一个完美的圆形 */
}

.value {
    font-size: 13px;
    font-weight: 400;
    color: #3eaf7c;
}



.box-card {
    width: 90%;
    margin: 0;
    min-height: 230px;
}

.item {
    padding: auto;
}

.log-div {
    width: 46%;
    height: 99%;
    margin: 5px;
}

:deep(.el-table .warning-row) {
    --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

:deep(.el-table .success-row) {
    --el-table-tr-bg-color: var(--el-color-success-light-9);
}

:deep(.el-table .error-row) {

    --el-table-tr-bg-color: var(--el-color-error-light-9);
}

.podValue {
    margin-left: 20px;
}

.el-table .info-row {}
</style>
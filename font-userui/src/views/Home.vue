<template>
  <el-row>
    <el-col :span="8">
  <el-card class="box-card" style="width: 380px; height: 40vh;">
   <div class="user">
     <img :src="avatarSrc">
     <div class="user-info">
       <p class="name">{{ username }}</p>
       <p class="access">{{ getRole() }}</p>
     </div>
   </div>
   <div class="login-info">
     <div><p>上次登录时间 <span>{{ userLoginLog.lastLoginTime }}</span></p></div>
     <div><p>上次登录ip <span>{{ userLoginLog.lastLoginIp }}</span></p></div>
   </div>
  </el-card>
  <el-card style="margin-top: 20px; height: 410px">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="imageName" label="文件名"  />
      <el-table-column prop="date" label="上传日期"  />
      <el-table-column prop="referenceCount" label="总引用" />
    </el-table>
  </el-card>
    </el-col>
    <el-col :span="16">
      <div class="num">
        <el-card v-for="item in labelDate" :key="item.label" :body-style="{display: 'flex',padding: '0',height : '80px'}">
          <div class="icon" :style="{backgroundColor:item.color}">
          <el-icon size="30px" style="text-align: center">
            <component  :is=item.icon ></component>
          </el-icon>
          </div>
          <div class="details">
            <p class="sum">{{item.sum}}次</p>
            <p class="label">{{item.label}}</p>
          </div>
        </el-card>
      </div>
      <div style="margin-left: 10px">
      <el-card style="height: 260px">
        <div id="chart1" style="height: 260px"></div>
      </el-card >
      <div class="graph">
        <el-card id="chart2" style="height: 260px"></el-card>
        <el-card id="chart3" style="height:260px"></el-card>
      </div>
      </div>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import { getHomeDataApi } from "@/api/home/home";
import { FileStatistics, HomeData, UserLoginLog } from "@/api/home/type";
import useUserStore from "@/store/module/user";
import { DateTimeConverter } from "@/utils/time";
import {Delete, Link, UploadFilled} from "@element-plus/icons-vue";
import * as echarts from 'echarts';
import {onMounted, reactive, ref} from "vue";
import {avatarSrc} from "@/compositionApi/comUser"


const username = ref(useUserStore().user.username);
const userLoginLog:UserLoginLog = reactive({
    id  : 0, // 可选属性
    lastLoginTime: new Date(),
    lastLoginIp: '0',
    lastLoginDevice: "0.0.0.0",
    userUuid: "0.0.0.0"
})

const getRole = () => {
  console.log(useUserStore().user.level)
   switch(useUserStore().user.level){
     case 0 :
      return "普通用户";
     case 9 :
      return "管理员";
      default:
      return "其他"
   }
};




const  labelDate = reactive([
  {
    label : "今日上传",
    icon : UploadFilled,
    sum : 0,
    color : "#2CC6C9"
  },
  {
    label : "今日引用",
    icon : Link,
    sum : 0,
    color: "#69B6F4"
  },
  {
    label : "今日删除",
    icon : Delete,
    sum : 0,
    color : "#FFBC7E"
  },
  {
    label : "总上传",
    icon : UploadFilled,
    sum : 0,
    color: "#2CC6C9"
  },
  {
    label : "总引用",
    icon : Link,
    sum : 0,
    color: "#69B6F4"
  },{
    label : "总删除",
    icon : Delete,
    sum : 0,
    color: "#FFBC7E"
  }

])

const tableData: FileStatistics[] = reactive([]);

onMounted(async () => {
    const res   = await getHomeDataApi();
    let homeData = null;
    if(res.statusCode === 200){
      homeData = res.data;
      //设置登录日志
      Object.assign(userLoginLog,homeData.userLoginLog)
      userLoginLog.lastLoginTime = DateTimeConverter.toCustomFormat(userLoginLog.lastLoginTime);
      console.log(userLoginLog.lastLoginTime)
      //设置左侧表格
      for(const element of homeData.newFileStatistics){
        element.date = DateTimeConverter.toCustomDayFormat(element.date);
        tableData.push(element);
      }
      //设置六个标签
      if (homeData.sevenTotalStats[0]) {
       labelDate[0].sum = homeData.sevenTotalStats[0].totalUpload;
       labelDate[1].sum = homeData.sevenTotalStats[0].totalDownload;
       labelDate[2].sum = homeData.sevenTotalStats[0].totalDelete;
      }

      if (homeData.totalStats) {
       labelDate[3].sum = homeData.totalStats.totalUpload;
       labelDate[4].sum = homeData.totalStats.totalDownload;
       labelDate[5].sum = homeData.totalStats.totalDelete;
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
  boundaryGap: false,
  data: getSevenDate()
},
yAxis: {
  type: 'value'
},
series: 
  getSevenData(homeData)

};
const myCharts = echarts.init(document.getElementById('chart1'));
myCharts.setOption(option);

      
      
    }
    

  // 柱状图
  const echarts2 = echarts.init(document.getElementById('chart2'));
  const option2 = {
    legend: {
      // 图例文字颜色
      textStyle: {
        color: "#333",
      },
    },
    grid: {
      left: "20%",
    },
    // 提示框
    tooltip: {
      trigger: "axis",
    },
    xAxis: {
      type: "category", // 类目轴
      data: getSevenMonthDayDate(),
      axisLine: {
        lineStyle: {
          color: "#17b3a3",
        },
      },
      axisLabel: {
        interval: 0,
        color: "#333",
      },
    },
    yAxis: [
      {
        type: "value",
        axisLine: {
          lineStyle: {
            color: "#17b3a3",
          },
        },
      },
    ],
    color: ["#2ec7c9", "#b6a2de"],
    series: getSevenTwoData(homeData)
  };
  echarts2.setOption(option2);
  const option3 = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '登录设备统计',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 0, name: 'Mac' },
          { value: 0, name: 'Windows' },
          { value: 0, name: 'Linux' },
          { value: 0, name: 'Ios' },
          { value: 0, name: 'Android' },
          { value: 0, name: 'Other' }
        ]
      }
    ]
  };
  for(const element of option3.series[0].data){
    const name = element.name.toLocaleLowerCase();
    element.value = homeData?.deviceStatistics[name];
  }
 

  
  //饼图
  const echarts3 = echarts.init(document.getElementById('chart3'));
  option3 && echarts3.setOption(option3);
});
const getSevenDate = () =>{
  var currentDate = new Date();

// 创建一个数组来存储最近七天的日期
  var recentDays:Date[] = [];

// 循环七次，依次获取最近七天的日期
  for (var i = 0; i < 7; i++) {
  var day = new Date(currentDate);
  day.setDate(currentDate.getDate() - i); // 减去i天
  recentDays.push(day);
  }
  var resultDate:string[] = [];
  for(const ele of recentDays){
    resultDate.unshift(DateTimeConverter.toCustomDayFormat(ele));
}

  return resultDate;

}
const getSevenMonthDayDate = () =>{
  var currentDate = new Date();

// 创建一个数组来存储最近七天的日期
  var recentDays:Date[] = [];

// 循环七次，依次获取最近七天的日期
  for (var i = 0; i < 7; i++) {
  var day = new Date(currentDate);
  day.setDate(currentDate.getDate() - i); // 减去i天
  recentDays.push(day);
  }
  var resultDate:string[] = [];
  for(const ele of recentDays){
    resultDate.unshift(DateTimeConverter.toCustomMonthDayFormat(ele));
}

  return resultDate;

}
//获取折线图数据
const getSevenData = (homeData : HomeData) =>{
     const data0 =  {
        name: '上传',
        type: 'line',
        data: new Array()
      }
      const data1 =  {
        name: '引用',
        type: 'line',
        data: new Array()
      }
      const data2 = {
        name: '删除',
        type: 'line',
        data: new Array()
      }
      
    console.log(homeData.sevenTotalStats)
    let currentDay = new Date();
   for(let i = 0; i < 7; i++){
    let flag = false;
    currentDay.setDate(currentDay.getDate() - i);
    for(const element of homeData.sevenTotalStats){
    
      if(DateTimeConverter.isSameDay(currentDay,new Date(element.date))){
        flag = true;
        const update = element.totalUpload? element.totalUpload : 0
        data0.data.unshift(update);
        const link = element.totalDownload? element.totalDownload : 0
        data1.data.unshift(link);
        const deleteDate = element.totalDelete? element.totalDelete : 0
        data2.data.unshift(deleteDate);
      }
      
    }
    if(flag === true){
        flag =false;
        continue;
      }
      data0.data.unshift(0);
      data1.data.unshift(0);
      data2.data.unshift(0);
   }
   console.log(data0)
   console.log(data1)
   console.log(data2)
   return [data0,data1,data2];
   
 }
 
 const getSevenTwoData = (homeData : HomeData) =>{
     const data0 =  {
        name: '上传',
        type: 'bar',
        data: new Array()
      }
      const data1 =  {
        name: '引用',
        type: 'bar',
        data: new Array()
      }

      
    console.log(homeData.sevenTotalStats)
    let currentDay = new Date();
   for(let i = 0; i < 7; i++){
    let flag = false;
    currentDay.setDate(currentDay.getDate() - i);
    for(const element of homeData.sevenTotalStats){
    
      if(DateTimeConverter.isSameDay(currentDay,new Date(element.date))){
        flag = true;
        const update = element.totalUpload? element.totalUpload : 0
        data0.data.unshift(update);
        const link = element.totalDownload? element.totalDownload : 0
        data1.data.unshift(link);
      }
      
    }
    if(flag === true){
        flag =false;
        continue;
      }
      data0.data.unshift(0);
      data1.data.unshift(0);
   }
   console.log(data0)
   console.log(data1)
   return [data0,data1];
   
 }

</script>

<style lang="less" scoped>

.login-info{
  p{
    line-height: 28px;
    font-size: 14px;
    color : #999999;
    span{
      color: #666666;
      margin-left: 60px;
    }
  }
}
.user{
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ccc;
  display: flex;
  align-items: center;
  img{
    margin-right: 40px;
    width: 150px;
    height: 150px;
    border-radius: 50%;
  }
  .user-info{
    .name{
      font-size: 32px;
      margin-bottom: 10px;
    }
    .access{
      color: #999999;
    }
  }

}
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

.num {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .icon {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 80px;
    height: 80px;
    text-align: center;
    line-height: 80px;
    color: #fff;
  }
  .details {
    margin-left: 15px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .sum {
      font-size: 20px;
      margin-bottom: 10px;
      line-height: 30px;
      height: 30px;
    }
    .label {
      font-size: 14px;
      color: #999;
      text-align: center;
    }
  }
  .el-card {
    margin-left: 5px;
    width: 32%;
    margin-bottom: 20px;
  }
}
.graph{
  display: flex;
  margin-top: 20px;
  justify-content: space-between;
  .el-card{
    width: 48%;

  }
}

</style>

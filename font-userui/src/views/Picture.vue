<template>
  <div @contextmenu.prevent="">
    <div class="bt">
      
      <el-button link  @click="back" v-if="path.length >= 1">
        <el-icon>
          <ArrowLeft/>
        </el-icon>
        返回上一级
      </el-button>
      <el-button type="primary" size="small" @click="dialogVisible = true" :icon=Upload>上传图片</el-button>
      <el-button size="small"  @click="addFolder = true">
        新建文件夹
      </el-button>
      <el-dialog
          title="输入文件名字"
          v-model="addFolder"
          width="20%"
      >
        <el-input v-model="fileName" placeholder="请输入内容" :input-style="{}"></el-input>
        <span slot="footer" class="dialog-footer">
            <el-button @click="addFolder = false">取 消</el-button>
            <el-button type="primary" @click="newFolder">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 重命名框 -->
      <el-dialog
          title="输入新的名称"
          v-model="dialogRenameVisible"
          width="20%"
      >
        <el-input v-model="renameText" placeholder="请输入内容" :input-style="{}"></el-input>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogRenameVisible = false">取 消</el-button>
            <el-button type="primary" @click="onAgreeRename">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog
          title="上传"
          v-model="dialogVisible"
          width="30%"
          :before-close="handleClose">
        <el-upload
            ref="refUpload"
            class="upload-demo"
            drag
            :before-upload="beforeUpload"
            :v-loading="loading"
            :http-request="toUploadImg"
            :limit = 1
            :multiple = false
            element-loading-text="正在上传"
        >
          <el-icon :size="20">
            <Upload />
          </el-icon>
          <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip"></div>
        </el-upload>
        <span slot="footer" class="dialog-footer">
  </span>
      </el-dialog>
      <el-dialog v-model="deleteDialogVisible" title="提示" width="25%">
      <span>确定要删除吗？</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="onAgreeDelete">确定</el-button>
        </span>
      </template>
    </el-dialog>
      <div id="search">
        <input placeholder="请输入内容" class="search" v-model="keywords" />
        <el-icon v-if="keywords.length > 0" style="margin-right: 10px">
          <Close @click="backSearch" />
        </el-icon>

        <el-icon >
          <Search/>
        </el-icon>

      </div>
    </div>
    <el-table  v-loading="loading" :data="filteredTableData" :height="height" :max-height="1080">
      <el-table-column    prop="name" label="文件名" width="580px">
        <template  v-slot="{row}">
          <img  @contextmenu.prevent="onContextMenu($event,row)" :src=thumbnail(row) class="img-mini" />
          <a href="javascript:void(0)" @contextmenu.prevent="onContextMenu($event,row)" class="fileName" @click="next(row)">{{ row.name }}</a>
        </template>
      </el-table-column>
  
      <el-table-column >
<!--        几个自定义图标-->
        <template v-slot="{row}">
          <div >
            <el-button  type="primary" v-if="isPicture(row)" round size="small" style="margin-right: 50px" @click="previewEvent(row)">预览</el-button>
            <!-- 在弹出窗口中显示图片 -->

            <el-tooltip v-if="isPicture(row)"  class="item" effect="dark" content="引用" placement="bottom-start">
            <el-button link @click="getDirectLink(row)">
              <el-icon>
                <Link/>
              </el-icon>
            </el-button>
           </el-tooltip>
          <el-tooltip v-if="isPicture(row)"  class="item" effect="dark" content="下载" placement="bottom-start">
            <el-button link>
              <el-icon @click="downImg(row)">
                <Download/>
              </el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip  class="item" effect="dark" content="删除" placement="bottom-start" >
            <el-button link><el-button link>
              <el-icon @click = "appearDelete(row)">
                <Delete />
              </el-icon>
            </el-button></el-button>
          </el-tooltip>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="size" label="大小" width="270"></el-table-column>
      <el-table-column prop="time" label="修改日期" width="220"></el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts" setup>
import {Upload, Search, Download, Delete, Link, ArrowLeft, Close} from "@element-plus/icons-vue";
import {computed, onMounted, ref} from "vue";
import {createFolder, uploadImg,getPathFiles, deleteFileApi, mvFilesApi} from '@/api/user/image';
import { ApiResponse } from "@/utils/data";
import { ElMessage, UploadInstance, UploadRawFile, UploadRequestOptions } from "element-plus";
import { preview } from "vue3-image-preview";
import { Folder, TableElement, TableElementOrNull, image } from "@/api/user/type";
import { timestampToTime } from "@/utils/time";
import useLoadingStore from "@/store/module/loading";
import ContextMenu from '@imengyu/vue3-context-menu';
import { isValidFileName } from "@/utils/verification";
import { behindURL } from "@/utils/request";
import useUserStore from "@/store/module/user";
import axios from "axios";

//刷新页面数据
const  flushTableData = () => {
  console.log(1)
  let requestPath = "/";
  if(path.value.length === 1){
    requestPath = requestPath + path.value[0];
  }else if(path.value.length > 1){
    requestPath = requestPath + path.value.join("/");
  }
  tableData.value.length = 0;
  getPathFiles(requestPath).then(
      (res) => {    
      const folderArr : Folder[]  = res.data.folderList;
      const imageArr : image[] = res.data.imageList;
      for(const element of  folderArr){
        tableData.value.push({name:element.folderName,img : 'img1',size : '-',md5 : null,time : timestampToTime(element.lastModifiedAt),thumbnailData :null,url:null})
      }
      const userStore = useUserStore();
      for(const element of  imageArr){
        const nameArr :string[] =  element.imageName.split(".");
        const url = behindURL+"/user/getImg?Authorization="+userStore.user.token+"&metaFileName=" + element.md5+"."+ nameArr[nameArr.length - 1];
        tableData.value.push({name:element.imageName,img : 'img2',size : element.imageSize,md5 : element.md5,time : timestampToTime(element.lastModifiedAt),thumbnailData : element.thumbnailData,url : url})
      }
    }
  )
}
//请求首页数据
onMounted(() =>{
  flushTableData();
}
);


//请求文件数据
//核心图片数据区
let tableData   = ref<TableElement[]>([
]);
const  height =  window.innerHeight - 62 - 80 - 40;
let keywords = ref('');
//当前文件夹目录
let path =  ref<string[]>([]);
const joinPath = (fileName : string) => {
  if(path.value.length == 0){
    return "/" + fileName;
  }else{
    return "/" + path.value.join("/") + "/" + fileName;
  }
}
const next = (row: TableElement) => {
  if(row.size.length > 1){
    return;
  }
  path.value.push(row.name);
  flushTableData();
}

//预览文件按钮

    
const previewEvent = (row: TableElement) => {
  
    preview({
      images: row.url
    });
};
    

  //创建新文件夹
const addFolder = ref(false);
const fileName = ref();
const  dialogVisible = ref(false);

const newFolder  = () => {
  if(!isValidFileName(fileName.value)){
    ElMessage.error("文件名不能包括特殊字符")
    return;
  }
  for(const element of tableData.value){
    if(element.name === fileName.value){
      ElMessage.error("目录已经存在")
     return; 
    }
  }
  let sendPath:string = '/' + fileName.value;
  if(path.value.length > 0){
    sendPath ="/" + path.value.join("/") + sendPath ;
  }
  createFolder(sendPath).then(
    (res : ApiResponse) => {
    if(res.statusCode === 200){
      ElMessage({
        message: '创建成功',
        type: 'success',
      });
    flushTableData();
    };
  }

  )
};
const back = () => {
  tableData.value.length = 0;
  if(path.value.length == 0){
    return
  }
  path.value.pop();
  flushTableData();
}
const backSearch = () =>{
  keywords.value = '';
}
 
const loading = computed(() => {
  return useLoadingStore().loading;
})
//接口请求过滤的数据
const filteredTableData = computed(() => {
  if (!keywords.value) {
    return tableData.value;
  }
  const lowerKeywords = keywords.value.toLowerCase();
  return tableData.value.filter(item => item.name.toLowerCase().includes(lowerKeywords));
});
//验证图片方法
const isImage = (file: UploadRawFile) => {
  // 获取文件的扩展名（后缀）
  const ext = file.name.slice(((file.name.lastIndexOf(".") - 1) >>> 0) + 2);
  // 定义支持的图片文件后缀
  const imageExtensions = ["jpg", "jpeg", "png", "gif", "bmp"];
  // 判断文件后缀是否为图片格式，并且文件大小不超过 50MB
  return imageExtensions.includes(ext.toLowerCase()) && file.size <= 50 * 1024 * 1024;
}
//验证是否存在
const isHave = (file: UploadRawFile) => {
  // 获取文件的扩展名（后缀）
  for(const element of tableData.value){
    if(element.name == file.name){
      return true;
    }
  }
  return false;
  // 判断文件后缀是否为图片格式，并且文件大小不超过 50MB
}
const beforeUpload = (rawFile: UploadRawFile) =>{
  if(!isValidFileName(rawFile.name)){
    ElMessage.error("文件名不能包括特殊字符")
    return false;
  }
  
  if(!isImage(rawFile)){
    ElMessage.error("上传图片只能是不大于50MB的 JPG、JPEG、PNG 格式!")
    return false;
  }
  if(isHave(rawFile)){
    ElMessage.error("不能上传已经存在的文件")
    return false;
  }
}
//获取上传子组件实例
const img = ref<UploadInstance>();

//关闭情况列表
const handleClose = () => {
  img.value?.clearFiles();
  dialogVisible.value=false;
}
const downImg = (row : TableElement) => {
  const customHeaders = {
  'Authorization': useUserStore().user.token,
};
  const filenameParts = row.name.split(".");
  const suffix = filenameParts[filenameParts.length - 1];
  const md5MetaFileName = row.md5 + "." + suffix;
  axios.get(behindURL+"/user/downImg", { 
    headers : customHeaders,
    responseType: 'blob',
    params :{
      md5MetaFileName
    }
   })
  .then(response => {
    // 检查请求是否成功
    if (response.status === 200) {
      // 获取后端返回的文件数据
      const fileData = response.data;

      // 创建一个URL对象，表示文件数据
      const url = window.URL.createObjectURL(new Blob([fileData]));

      // 创建一个虚拟的下载链接
      const link = document.createElement('a');
      link.href = url;

      // 设置下载链接的属性
      link.setAttribute('download', row.name); // 文件名

      // 模拟用户点击下载链接
      link.click();

      // 释放URL对象
      window.URL.revokeObjectURL(url);
    } else {
      console.error('Download request failed:', response.status);
    }
  })
  .catch(error => {
    console.error('Error:', error);
  });
}
const appearDelete = (row : TableElement) => {
  deleteRow = row;
  deleteDialogVisible.value = true;
}
let deleteRow : TableElementOrNull = null;
//右键菜单
const onContextMenu = (e : MouseEvent,row : TableElement) => {
  //prevent the browser's default menu
  e.preventDefault();
  //show your menu
  ContextMenu.showContextMenu({
    x: e.x,
    y: e.y,
    items: [
      {
        label: "获取图片直链", 
        hidden : !isPicture(row),
        onClick: () => {
          getDirectLink(row);
        }
      },
      {
        label: "重命名", 
        onClick: () => {
          appearRename();
          oldPath = joinPath(row.name);
        }
      },
      
      { 
        label: "删除",  
        onClick: () => {
          console.log("执行了删除")
          appearDelete(row); 
        }
      },
    ]
  }); 
}
const getDirectLink = async (row : any) => {
  try {
    await navigator.clipboard.writeText(row.url);
    ElMessage.success('直链复制到剪贴板');
  } catch (err) {
    ElMessage.warning('获取直链失败');
  }
}
//重命名方法
const dialogRenameVisible = ref(false);
let renameText = ref();
let oldPath = "";
const appearRename = () => {
  dialogRenameVisible.value = true;
}
const onAgreeRename = () =>{
  const newPath = joinPath(renameText.value);
  const params = new URLSearchParams();
  params.append("oldPath",oldPath);
  params.append("newPath",newPath);
  if(!haveSameExtension(oldPath,newPath)){
    ElMessage.error("新旧文件后缀必须相同")
    return;
  }
  renameText.value = "";
  mvFilesApi(params).then(
    (res) => {
      if(res.statusCode === 200){
        ElMessage.success(res.data);
        flushTableData();
      }
    }
  )
}
function haveSameExtension(file1: string, file2: string): boolean {
  const getExtension = (fileName: string) => {
    const dotIndex = fileName.lastIndexOf('.');
    // 如果没有点或点是最后一个字符，则没有扩展名
    if (dotIndex === -1 || dotIndex === fileName.length - 1) {
      return null;
    }
    return fileName.substring(dotIndex + 1);
  };

  const ext1 = getExtension(file1);
  const ext2 = getExtension(file2);

  // 如果两个文件都没有扩展名，我们认为它们的扩展名是相同的
  if (ext1 === null && ext2 === null) {
    return true;
  }

  // 比较两个扩展名
  return ext1 === ext2;
}
const onAgreeDelete = () => {
  if(deleteRow != null){
 deleteFile(deleteRow);
  }
  deleteRow = null;
   flushTableData(); 
   deleteDialogVisible.value = false;
}
const refUpload = ref();
let deleteDialogVisible = ref(false);
//发送图片方法
const toUploadImg = (param: UploadRequestOptions) => {
  const file = param.file;
  const currentPath = "/" + path.value.join("/");
  const formData = new FormData();
  formData.append('file', file); // 这里可以根据需要设置其他表单字段
  formData.append('path',currentPath);
  uploadImg(formData).then(
    (res) => {
    if(res.statusCode === 200){
      ElMessage.success('上传成功');
      flushTableData();
      refUpload.value.clearFiles();
    }
    
    
  })
  return formData;
}

const  deleteFile = (row : TableElement) => {
  const formData = new FormData();
  let sendPath = null;
  if(path.value.length > 0){
    sendPath = "/" + path.value.join("/") + "/" + row.name;
  }else{
    sendPath = "/" + row.name;
  }
  
  formData.append("path",sendPath);
  deleteFileApi(formData).then(
    (res) =>{
      if(res.statusCode === 200){
        ElMessage.success(res.data);
        flushTableData();
      }else{
        ElMessage.error(res.message);
      }
    }
  )
}



//是否是图片
const isPicture = (row: { size: string | any[]; }) => {
  return row.size.length > 1;
}
//获取缩略图
const thumbnail = (row : any) => {
  if(row.size.length > 1){
    return `data:image/jpeg;base64,${row.thumbnailData}`
  }else{
    return "../src/static/img/folder.svg";
  }
}


</script>



<style lang="less" scoped>

.bt {
  max-width: 100%;
  background-color: white;
  height: 40px;
  font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
  line-height: 30px;
}
.nav{
  max-width: 100%;
  background-color: white;
  height: 20px;
  /*font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";*/
  font-size: 8px;
  line-height: 20px;
}

.el-table-column {
  max-height: 48px;
  line-height: 48px;
}

.el-table {
  max-width: 100%;
  font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
}

#search {
  width: 315px;
  border-radius: 33px;
  background-color: #f7f7f7;
  float: right;
  text-align: center;
  height: 32px;
  line-height: 32px;
  input{
     outline: none;

  }

}

.search {
  border: none;
  background-color: #f7f7f7;
  height: 30px;
  width: 248px;
}

img {
  width: 30px;
  height: 30px;
}

a {
  color: #424e67;
  text-decoration: none;
}

a:hover {
  color: #09AAFF;
}
.el-icon-delete{
  color:#F56C6C;
}
.dialog-footer{
  display: flex;
  justify-content: space-between;

}
span{
  margin-top: 5%;
}
.img-mini{
  display: inline-block;
  vertical-align: middle
}
.fileName{
  margin-left: 5px;
}

</style>
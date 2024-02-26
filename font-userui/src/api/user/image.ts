import { ApiResponse } from "@/utils/data";
import service from "@/utils/request";
import { Folder, FolderModel, PageFiles } from "./type";
const baseURL = 'user';

//创建文件夹
export const createFolder = (newPath : string) => {
  const prams = new URLSearchParams();
  prams.append('newPath',newPath);
  return service<any, ApiResponse>({ 
    url: baseURL + '/createFolder',
    method: 'post',
    'data': prams,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
//上传图片
export const uploadImg = (formData:FormData) => {
  return service<any, ApiResponse>({ 
    url: baseURL + '/uploadImg',
    method: 'post',
    'data': formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
export const getPathFiles = (path : string) => {
  return service<any, ApiResponse<PageFiles>>({ 
    url: baseURL + '/getPageFiles',
    method: 'get',
    params:{
      path
    }
  })
}
export const downFileApi = (fileName : string) => {
  return service<any, ApiResponse<string>>({ 
    url: baseURL + '/downImg',
    params : {
      fileName
    }
    
  })
}
export const deleteFileApi = (formData : FormData) => {
  return service<any, ApiResponse<string>>({ 
    url: baseURL + '/deleteFiles',
    method: 'delete',
    data : formData,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
    
  })
}
export const mvFilesApi = (params : URLSearchParams) => {
  return service<any, ApiResponse<string>>({ 
    url: baseURL + '/mvFiles',
    method: 'put',
    data : params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
    
  })
}

export const getAllFolders = () => {
  return service<any, ApiResponse<FolderModel>>({ 
    url: baseURL + '/getAllFolders',
    method: 'get',
  })
}
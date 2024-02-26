import { ApiResponse } from "@/utils/data";
import service from "@/utils/request";
import { DirectLinkToken } from "./type";
import { Folder } from "../user/type";
const baseURL = 'user';
export const getDirectLinkToken = () => {
    return service<any, ApiResponse<DirectLinkToken>>({ 
      url: baseURL + '/getPicgoToken',
      method: 'get',
    })
  }

  export const putDirectLinkToken = (data : DirectLinkToken) => {
    return service<any, ApiResponse<DirectLinkToken>>({ 
      url: baseURL + '/putPicgoToken',
      method: 'put',
      data
    })
  }

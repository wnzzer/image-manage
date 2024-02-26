import { ApiResponse } from "@/utils/data";
import service from "@/utils/request";
import { HomeData } from "./type";
const baseURL = 'user';
export const getHomeDataApi = () => {
    return service<any, ApiResponse<HomeData>>({ 
      url: baseURL + '/getHomeData',
      method: 'get',
    })
  }
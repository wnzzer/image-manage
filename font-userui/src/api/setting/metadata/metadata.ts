import { ApiResponse } from "@/utils/data"
import service from "@/utils/request"
import { PodMetaData } from "./type"

export const getMetaDataApi = () => {
    return service<any, ApiResponse<PodMetaData>>({
      url: '/admin/getMetaData',
      method: 'get'
    })
  }
import { ApiResponse } from "@/utils/data";
import service from "@/utils/request";
export const login = (data : any) => {
    return service<any,ApiResponse>({
      url: '/login',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }

  export const registerApi = (username : string,password : string) => {
    return service<any,ApiResponse>({
      url: '/register',
      method: 'post',
      data : {username,password},
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }
  
  export const isClusterApi = () => {
    return service<any,ApiResponse<{isClusterModeEnabled : string}>>({
      url : '/admin/isClusterMode'
    })
  }
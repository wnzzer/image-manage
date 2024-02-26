import { ApiResponse } from "@/utils/data";
import service from "@/utils/request";
import { User } from "../setting/user/type";
const baseURL = 'user';
export const putAvatarApi = (avatar: string | Blob) => {
  const data = new FormData();
  data.append('avatar', avatar);
  return service<any, ApiResponse<User>>({
    url: baseURL + '/putAvatar',
    method: 'put',
    data,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}


export const putPasswordApi = (newPassword : string) => {
  return service<any, ApiResponse<User>>({
    url: baseURL + '/putPassword',
    method: 'put',
    data : {newPassword},
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
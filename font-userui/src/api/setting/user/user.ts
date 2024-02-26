import { ApiResponse } from "@/utils/data";
import service from "@/utils/request";
import  {SimplyUser, User } from "./type";
export const getUsers = () => {
  return service<any, ApiResponse<SimplyUser[]>>({
    url: '/admin/getAllUsers',
    method: 'get'
  })
}

export const addUser = (user : User) => {
    return service<any, ApiResponse<string>>({
    url: '/admin/addUser',
    method: 'post',
    data : user
  })
}

export const deleteUser = (id : number) => {
  return service<any, ApiResponse<string>>({
    url: '/admin/deleteUser',
    method: 'delete',
    params : {id}
  })
}

export const putUser = (user: User) => {
  return service<any, ApiResponse<string>>({
    url: '/admin/putUser',
    method: 'put',
    data : user
  })
}
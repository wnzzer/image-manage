import { PageFiles } from "@/api/user/type";
import type {
  AxiosRequestConfig,
  AxiosRequestHeaders,
  AxiosError,
} from "axios";

// 自定义请求接口headers头参数类型
type RequestHeader = AxiosRequestHeaders & { Authorization?: string }

// 自定义请求接口request参数类型，可以加一些自己自定义的参数
export interface RequestConfig extends AxiosRequestConfig {
  headers : AxiosRequestHeaders // 放入请求头
  noNeedToken?: boolean // 该接口是否需要token
}
export interface RequestInterceptorsConfig extends RequestConfig { // 请求拦截器使用
  headers: RequestHeader;
}

// 主体后端返回格式
export interface ApiResponse<T = any> {
  statusCode: number;
  message: string;
  data: T; // 这里定义请求返回data数据类型
}

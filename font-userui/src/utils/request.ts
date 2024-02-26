import axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import router from '@/router';
import useUserStore from '@/store/module/user';
import { ElMessage } from 'element-plus';
import useLoadingStore from '@/store/module/loading';
import { loadConfig } from '@/api/getConfig';
const res = await loadConfig();
export  const behindAddress = res.API_URL;
console.log(behindAddress)
export const behindURL = behindAddress + '/api';
const service: AxiosInstance = axios.create({
  baseURL: behindURL, // 设置API的基本URL
  timeout: 5000, // 设置请求超时时间
});

// 创建一个Map来存储正在进行的请求
const pendingRequests = new Map();

// 创建一个请求取消的函数
const cancelRequest = (config: InternalAxiosRequestConfig<any>) => {
  const requestId = getRequestIdentifier(config);

  if (pendingRequests.has(requestId)) {
    const cancelToken = pendingRequests.get(requestId);
    cancelToken.cancel('Request canceled due to duplicate request.');
    pendingRequests.delete(requestId);
  }
};

// 创建一个唯一请求标识符
const getRequestIdentifier = (config: InternalAxiosRequestConfig<any>) => {
  return `${config.method}_${config.url}`;
};

// 请求拦截器
service.interceptors.request.use(
  (config) => {

    //处理重复请求
    cancelRequest(config);
    const requestId = getRequestIdentifier(config);
    const cancelToken = axios.CancelToken.source();
    config.cancelToken = cancelToken.token;
    pendingRequests.set(requestId, cancelToken);

    //加载动画
    const loadingState = useLoadingStore();
    loadingState.loading = true;
    //如果持久化导致大菠萝数据异常，就清除
    if (useUserStore().user == null) {
      useUserStore().clearToken();
    }
    const token = useUserStore().user.token;

    if (token != null && token.length > 0) {
      config.headers.Authorization = token;
    }
    // 在发送请求之前可以添加请求头等操作
    return config;
  },
  (error) => {
    const loadingState = useLoadingStore();
    loadingState.loading = false;
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    //关闭动画
    const loadingState = useLoadingStore();
    loadingState.loading = false;

    //用于移除已完成的请求
    const requestId = getRequestIdentifier(response.config);
    pendingRequests.delete(requestId);
    // 在这里可以处理响应数据

    if (response.status === 200) {
      if (response.data.statusCode === 200) {
        return response.data;
      } else if (response.data.statusCode === 401) {
        // 执行清除 token 的操作
        useUserStore().clearToken();
        ElMessage({
          message: 'token已失效.',
          type: 'warning',
        });
        router.push('/login');
      } else {
        ElMessage({
          message: response.data.message,
          type: 'warning',
        });
      }

    }


  },
  (error) => {
    //关闭动画
    const loadingState = useLoadingStore();
    loadingState.loading = false;

    ElMessage({
      message: '接口访问错误.',
      type: 'error',
    });
    return Promise.reject(error);
  }
);

export default service;

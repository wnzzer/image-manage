import { ApiResponse } from "@/utils/data"
import service from "@/utils/request"

export const getPodLogsApi = (page : number) => {
    return service<any,ApiResponse<LoggingEvent[]>>({
        url : '/admin/getLogs',
        method : 'get',
        params : {page}
    })

}
export const getPodStatusApi = () => {
    return service<any,ApiResponse<PodsStatus>>({
        url : '/admin/getPodsStatus',
        method : 'get'
    })

}
export const getPodMetricsApi = () => {
    return service<any,ApiResponse<ResponsePodMetrics[]>>({
        url : '/admin/getMetrics',
        method : 'get'
    })

}
export const getClusterServiceApi = () => {
    return service<any,ApiResponse<{clusterIp :string, port : string}>>({
        url : '/admin/getClusterService',
        method : 'get'
    })

}
export const putReplicasApi = (count : number) => {
    return service<any,ApiResponse<string>>({
        params : {count},
        url : '/admin/putReplicas',
        method : 'put'
    })

}
package com.chen.behindimagesmanage.controller;

import com.chen.behindimagesmanage.annotation.CurrentUserUuid;
import com.chen.behindimagesmanage.config.KubernetesClientConfig;
import com.chen.behindimagesmanage.pojo.*;
import com.chen.behindimagesmanage.service.AdminService;
import com.chen.behindimagesmanage.service.KubernetesService;
import com.chen.behindimagesmanage.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.NonNull;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 15031
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private KubernetesService kubernetesService;
    @GetMapping("/getAllUsers")
    public ApiResponse<List<SimplyUser>> getAllUsers(){
        return adminService.getUsers();
    }
    @PostMapping("/addUser")
    public ApiResponse<String> addUser(@RequestBody @NonNull User user, @CurrentUserUuid String userUuid){
       return adminService.addUser(user,userUuid);

    }

    @DeleteMapping("/deleteUser")
    public ApiResponse<String> deleteUser(@NonNull Integer id,@CurrentUserUuid String userUuid){
        return adminService.deleteUser(id,userUuid);
    }

    @PutMapping("/putUser")
    public ApiResponse<String>putUser(@RequestBody @NonNull User user,@CurrentUserUuid String userUuid){
        return adminService.putUser(user, userUuid);
    }


    @GetMapping("/isClusterMode")
    public ApiResponse<Map<String,Boolean>>isClusterModeEnabled(){
        return kubernetesService.isClusterMode();
    }

    @GetMapping("/getPodsStatus")
    public ApiResponse<PodsStatus>getPodsStatus(){
        return kubernetesService.getPodsStatus();
    }


    @GetMapping("/getMetaData")
    public ApiResponse<PodMetaData>getMetaData(){
        return kubernetesService.getMetaData();
    }

    @GetMapping("/getMetrics")
    public ApiResponse<List<ResponsePodMetrics>>getMetrics(){
        return kubernetesService.getMetrics();
    }

    @GetMapping("/getLogs")
    public ApiResponse<List<LoggingEvent>>getLogs(Integer page){
        return kubernetesService.getLogs(page);
    }

    @GetMapping("/getClusterService")
    public  ApiResponse<Map<String,String>> getClusterService(){
        return kubernetesService.getClusterService();
    }

    @PutMapping("putReplicas")
    public ApiResponse<String>putReplicas(int count){
        return  kubernetesService.putReplicas(count);
    }

}

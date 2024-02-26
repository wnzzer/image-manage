package com.chen.behindimagesmanage.config;

/**
 * @author 15031
 */

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KubernetesClientConfig {
    @Value("${config.k8s-role-config-path}")
    private String k8sRoleConfigPath;

    @Value("${config.isClusterModeEnabled}")
    private boolean isClusterModeEnabled;


    public boolean isClusterModeEnabled() {
        return isClusterModeEnabled;
    }
    @Bean
    public ApiClient apiClient() throws Exception {
        ApiClient client = Config.fromConfig(this.k8sRoleConfigPath);
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        return client;
    }

    @Bean
    public CoreV1Api coreV1Api(ApiClient apiClient) {
        return new CoreV1Api(apiClient);
    }

    @Bean
    public AppsV1Api appsV1Api(ApiClient apiClient) {
        return  new AppsV1Api(apiClient);
    }

}

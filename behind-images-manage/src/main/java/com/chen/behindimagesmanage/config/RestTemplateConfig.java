package com.chen.behindimagesmanage.config;

import io.kubernetes.client.openapi.ApiClient;
import io.netty.handler.codec.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author 15031
 */
@Configuration
public class RestTemplateConfig {
    private final ApiClient apiClient;

    public RestTemplateConfig(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public RestTemplate k8sRestTemplate() {
        RestTemplate k8sRestTemplate = new RestTemplate();
        k8sRestTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set(PodDataSynConfig.SY_KEY, PodDataSynConfig.SY_KEY_VALUE); // 自定义Header的设置
            return execution.execute(request, body);
        });
        return k8sRestTemplate;
    }
    @Bean
    public RestTemplate extendK8sRestTemplate() {
       return  new RestTemplate(new OkHttp3ClientHttpRequestFactory(apiClient.getHttpClient()));

    }

}

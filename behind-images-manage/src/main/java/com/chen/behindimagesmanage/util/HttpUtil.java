package com.chen.behindimagesmanage.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/** 这个工具类，几乎用于内部同步，默认加了同步token请求头
 * @author 15031
 */
@Component
public class HttpUtil {
    private final RestTemplate k8sRestTemplate;
    public  String questProtocol;


    public  String dnsSuffix;
    public  String splicePodUrl (String podName){
        return  questProtocol + podName + dnsSuffix;

    }

    public HttpUtil(RestTemplate k8sRestTemplate,@Value("${config.quest.protocol}") String questProtocol) {
        this.k8sRestTemplate = k8sRestTemplate;
        this.questProtocol = questProtocol;
    }


    public ResponseEntity<String> sendGetRequest(String url, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return k8sRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
    public ResponseEntity<File> sendGetRequest(String url) {
        return k8sRestTemplate.getForEntity(url,File.class);
    }

    public byte[] getBytes(String url) {
        return k8sRestTemplate.getForObject(url, byte[].class);
    }
    public ResponseEntity<byte[]> getBytesEntity(String url) {
        return k8sRestTemplate.getForEntity(url, byte[].class);
    }

    public ResponseEntity<String> sendPostRequest(String url, HttpEntity<String> entity) {
        return k8sRestTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
    public ResponseEntity<String> sendFormDataPostRequest(String url, HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        return k8sRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }
    public static String classifyUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "other";
        }

        String lowerCaseUA = userAgent.toLowerCase();

        if (lowerCaseUA.contains("windows")) {
            return "windows";
        } else if (lowerCaseUA.contains("macintosh") || lowerCaseUA.contains("mac os")) {
            return "mac";
        } else if (lowerCaseUA.contains("linux")) {
            return "linux";
        } else if (lowerCaseUA.contains("iphone") || lowerCaseUA.contains("ipad") || lowerCaseUA.contains("ipod")) {
            return "ios";
        } else if (lowerCaseUA.contains("android")) {
            return "android";
        } else {
            return "other";
        }
    }

    public static String getHeaderOrParameter(HttpServletRequest httpServletRequest,String headerName) {
        String token = null;
        token = httpServletRequest.getHeader(headerName);
        if(token == null){
            token = httpServletRequest.getParameter(headerName);
        }
        return token;

    }

}


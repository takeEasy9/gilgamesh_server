package com.gilgamesh.common.entity.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description web 拦截器配置
 * @createDate 2025/1/26 10:38
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "web.security.interceptor")
public class InterceptorProperty {
    /**
     * 请求白名单
     */
    private Set<String> whiteList;

    /**
     * 登录接口
     */
    private String loginApi;

    public Set<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(Set<String> whiteList) {
        this.whiteList = whiteList;
    }

    public String getLoginApi() {
        return loginApi;
    }

    public void setLoginApi(String loginApi) {
        this.loginApi = loginApi;
    }

    @Override
    public String toString() {
        return "InterceptorProperty{" +
                "whiteList=" + whiteList +
                ", loginApi='" + loginApi + '\'' +
                '}';
    }
}

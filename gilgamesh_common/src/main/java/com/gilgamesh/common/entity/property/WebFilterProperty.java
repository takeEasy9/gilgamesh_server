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
@ConfigurationProperties(prefix = "web.security.filter")
public class WebFilterProperty {
    /**
     * 请求白名单
     */
    private Set<String> whiteList;

    /**
     * 登录接口
     */
    private String loginApi;

    /**
     * 请求体最大长度限制, 单位字节
     */
    private int loginMaxRequestBodySize = 1024;

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

    public int getLoginMaxRequestBodySize() {
        return loginMaxRequestBodySize;
    }

    public void setLoginMaxRequestBodySize(int loginMaxRequestBodySize) {
        this.loginMaxRequestBodySize = loginMaxRequestBodySize;
    }

    @Override
    public String toString() {
        return "InterceptorProperty{" +
                "whiteList=" + whiteList +
                ", loginApi='" + loginApi + '\'' +
                ", loginMaxRequestBodySize=" + loginMaxRequestBodySize +
                '}';
    }
}

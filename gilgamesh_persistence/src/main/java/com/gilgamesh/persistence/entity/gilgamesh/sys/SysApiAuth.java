package com.gilgamesh.persistence.entity.gilgamesh.sys;


import com.gilgamesh.persistence.entity.base.GenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 接口权限信息
 * @createDate 2025/1/26 14:04
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_api_auth")
public class SysApiAuth extends GenericBaseEntity {
    @Serial
    private static final long serialVersionUID = 1286904124148356250L;

    /**
     * 接口权限中文名称
     */
    @Column(name = "api_auth_cn_name", unique = true)
    private String apiAuthCnName;

    /**
     * 接口权限英文文名称
     */
    @Column(name = "api_auth_en_name", unique = true)
    private String apiAuthEnName;

    /**
     * 接口权限描述
     */
    @Column(name = "api_auth_description")
    private String apiAuthDescription;

    /**
     * 接口请求方法, 如GET, POST
     */
    @Column(name = "api_method")
    private String apiMethod;

    /**
     * 接口路径
     */
    @Column(name = "api_path", nullable = true)
    private String apiPath;

    public String getApiAuthCnName() {
        return apiAuthCnName;
    }

    public void setApiAuthCnName(String apiAuthCnName) {
        this.apiAuthCnName = apiAuthCnName;
    }

    public String getApiAuthEnName() {
        return apiAuthEnName;
    }

    public void setApiAuthEnName(String apiAuthEnName) {
        this.apiAuthEnName = apiAuthEnName;
    }

    public String getApiAuthDescription() {
        return apiAuthDescription;
    }

    public void setApiAuthDescription(String apiAuthDescription) {
        this.apiAuthDescription = apiAuthDescription;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    @Override
    public String toString() {
        return "SysApiAuth{" +
                "super=" + super.toString() +
                ", apiAuthCnName='" + apiAuthCnName + '\'' +
                ", apiAuthEnName='" + apiAuthEnName + '\'' +
                ", apiAuthDescription='" + apiAuthDescription + '\'' +
                ", apiMethod='" + apiMethod + '\'' +
                ", apiPath='" + apiPath + '\'' +
                '}';
    }
}
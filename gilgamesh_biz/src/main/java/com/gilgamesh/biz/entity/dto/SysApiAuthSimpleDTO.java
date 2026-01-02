package com.gilgamesh.biz.entity.dto;

import com.gilgamesh.persistence.entity.gilgamesh.sys.SysApiAuth;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description
 * @createDate 2025/11/22 19:45
 * @since 1.0.0
 */
public class SysApiAuthSimpleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -391990969999638797L;
    /**
     * 接口权限ID
     */
    private Long apiAuthId;
    /**
     * 接口权限英文文名称
     */
    private String apiAuthEnName;

    /**
     * 接口请求方法, 如GET, POST
     */
    private String apiMethod;

    /**
     * 接口路径
     */
    private String apiPath;

    public SysApiAuthSimpleDTO(Long apiAuthId, String apiAuthEnName, String apiMethod, String apiPath) {
        this.apiAuthId = apiAuthId;
        this.apiAuthEnName = apiAuthEnName;
        this.apiMethod = apiMethod;
        this.apiPath = apiPath;
    }

    public SysApiAuthSimpleDTO(SysApiAuth sysApiAuth) {
        this.apiAuthId = sysApiAuth.getId();
        this.apiAuthEnName = sysApiAuth.getApiAuthEnName();
        this.apiMethod = sysApiAuth.getApiMethod();
        this.apiPath = sysApiAuth.getApiPath();
    }

    public Long getApiAuthId() {
        return apiAuthId;
    }

    public void setApiAuthId(Long apiAuthId) {
        this.apiAuthId = apiAuthId;
    }

    public String getApiAuthEnName() {
        return apiAuthEnName;
    }

    public void setApiAuthEnName(String apiAuthEnName) {
        this.apiAuthEnName = apiAuthEnName;
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
        return "SysApiAuthSimpleDTO{" +
                "apiAuthId='" + apiAuthId + '\'' +
                ", apiAuthEnName='" + apiAuthEnName + '\'' +
                ", apiMethod='" + apiMethod + '\'' +
                ", apiPath='" + apiPath + '\'' +
                '}';
    }
}

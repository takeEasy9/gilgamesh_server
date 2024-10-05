package com.gilgamesh.common.conditions;

import com.gilgamesh.common.utils.ConstantUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description api version condition
 * @createDate 2024/10/2 22:15
 * @since 1.0.0
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private final String version;

    public ApiVersionCondition(String version) {
        this.version = version;
    }

    @Override
    @NonNull
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    @Nullable
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = ConstantUtil.API_VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            String pathVersion = m.group(1);
            // 精确匹配
            if (Objects.equals(pathVersion, version)) {
                return this;
            }
        }
        return null;
    }

    @Override
    @NonNull
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return 0;
    }

    public String getApiVersion() {
        return version;
    }
}

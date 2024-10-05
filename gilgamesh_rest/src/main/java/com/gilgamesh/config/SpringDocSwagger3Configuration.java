package com.gilgamesh.config;

import com.gilgamesh.common.annotations.ApiVersion;
import com.gilgamesh.common.utils.ConstantUtil;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description swagger3 配置类
 * @createDate 2024/10/3 7:21
 * @since 1.0.0
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gilgamesh 接口文档",
                version = "1.0",
                description = "Gilgamesh Swagger3接口文档",
                contact = @Contact(name = "takeEasy9"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        security = @SecurityRequirement(name = "JWT"),
        servers = {
                @Server(url = "http://localhost:8000", description = "本地环境"),
                @Server(url = "http://192.168.96.233:8000", description = "开发环境"),
        },
        externalDocs = @ExternalDocumentation(description = "Swagger3 参考文档",
                url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"
        )
)
@SecurityScheme(name = "JWT", type = SecuritySchemeType.HTTP, description = "JWT认证", in = SecuritySchemeIn.HEADER, scheme = "bearer", bearerFormat = "JWT")
public class SpringDocSwagger3Configuration {
    private final Logger logger = LoggerFactory.getLogger(SpringDocSwagger3Configuration.class);

    @Bean
    public OpenApiCustomizer apiVersionCustomizer(RequestMappingHandlerMapping handlerMapping) {
        return openApi -> {
            Map<String, Map<RequestMethod, HandlerMethod>> originalUrlMethodHandlerMethodMap = getOriginalUrlMethodHandlerMethodMap(handlerMapping);
            Paths oldPaths = openApi.getPaths();
            Map<String, Map<RequestMethod, PathItem>> originalUrlMethodPathItemMap = getOriginalUrlMethodPathItemMap(oldPaths);
            Paths newPaths = new Paths();
            originalUrlMethodPathItemMap.forEach((originalUrl, requestMethodPathItemMap) -> {
                requestMethodPathItemMap.forEach(((requestMethod, pathItem) -> {
                    if (originalUrlMethodHandlerMethodMap.containsKey(originalUrl) && originalUrlMethodHandlerMethodMap.get(originalUrl).containsKey(requestMethod)) {
                        HandlerMethod handlerMethod = originalUrlMethodHandlerMethodMap.get(originalUrl).get(requestMethod);
                        // 优先获取方法上的版本注解, 方法上的版本注解不存在, 从controller上获取
                        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiVersion.class) != null ? AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiVersion.class) : AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), ApiVersion.class);
                        String version;
                        if (apiVersion == null) {
                            logger.error("api {} not annotated with apiVersion", originalUrl);
                            version = ConstantUtil.DEFAULT_VERSION;
                        } else {
                            version = apiVersion.value();
                        }
                        String newUrl = originalUrl.replace(ConstantUtil.API_VERSION_PLACEHOLDER, "v" + version);
                        if (!newPaths.containsKey(newUrl)) {
                            newPaths.addPathItem(newUrl, pathItem);
                        }
                    } else {
                        logger.error("openapi {}-{} not found in handlerMapping", requestMethod, originalUrl);
                    }
                }));
            });
            openApi.setPaths(newPaths);
        };
    }

    /**
     * 获取接口原始url与请求方法, handlerMethod映射
     *
     * @param handlerMapping RequestMappingHandlerMapping
     * @return Map<String, Map < RequestMethod, HandlerMethod>>
     */
    private Map<String, Map<RequestMethod, HandlerMethod>> getOriginalUrlMethodHandlerMethodMap(RequestMappingHandlerMapping handlerMapping) {
        Map<String, Map<RequestMethod, HandlerMethod>> originalUrlMethodHandlerMethodMap = new HashMap<>(16);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMapping.getHandlerMethods().entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            if (requestMappingInfo.getPathPatternsCondition() != null
                    && requestMappingInfo.getPathPatternsCondition().getFirstPattern().getPatternString().startsWith(ConstantUtil.API_PREFIX)) {
                String originalUrl = requestMappingInfo.getPathPatternsCondition().getFirstPattern().getPatternString();
                requestMappingInfo.getMethodsCondition().getMethods().forEach(requestMethod -> {
                    if (originalUrlMethodHandlerMethodMap.containsKey(originalUrl)) {
                        originalUrlMethodHandlerMethodMap.get(originalUrl).put(requestMethod, handlerMethod);
                    } else {
                        Map<RequestMethod, HandlerMethod> requestMethodHandlerMethodMap = new HashMap<>(16);
                        requestMethodHandlerMethodMap.put(requestMethod, handlerMethod);
                        originalUrlMethodHandlerMethodMap.put(originalUrl, requestMethodHandlerMethodMap);
                    }
                });
            }
        }
        return originalUrlMethodHandlerMethodMap;
    }

    /**
     * 获取接口原始url与请求方法, PathItem映射
     *
     * @param oldPaths Paths
     * @return Map<String, Map < RequestMethod, PathItem>>
     */
    private Map<String, Map<RequestMethod, PathItem>> getOriginalUrlMethodPathItemMap(Paths oldPaths) {
        Map<String, Map<RequestMethod, PathItem>> originalUrlMethodPathItemMap = new HashMap<>(16);
        oldPaths.forEach((originalUrl, pathItem) -> {
            pathItem.readOperationsMap().forEach(((httpMethod, operation) -> {
                RequestMethod requestMethod = RequestMethod.resolve(httpMethod.name());
                // 从原有的pathItem中复制属性
                PathItem newPathItem = new PathItem();
                newPathItem.setSummary(pathItem.getSummary());
                newPathItem.setDescription(pathItem.getDescription());
                newPathItem.operation(httpMethod, operation);
                newPathItem.setServers(pathItem.getServers());
                newPathItem.setParameters(pathItem.getParameters());
                newPathItem.setExtensions(pathItem.getExtensions());
                if (originalUrlMethodPathItemMap.containsKey(originalUrl)) {
                    originalUrlMethodPathItemMap.get(originalUrl).put(requestMethod, newPathItem);
                } else {
                    Map<RequestMethod, PathItem> requestMethodPathItemMap = new HashMap<>(16);
                    requestMethodPathItemMap.put(requestMethod, newPathItem);
                    originalUrlMethodPathItemMap.put(originalUrl, requestMethodPathItemMap);
                }
            }));
        });
        return originalUrlMethodPathItemMap;
    }
}

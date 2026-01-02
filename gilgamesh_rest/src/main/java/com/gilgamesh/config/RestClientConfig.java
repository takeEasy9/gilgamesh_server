package com.gilgamesh.config;

import com.gilgamesh.common.entity.property.RestClientPoolProperties;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description restClient 配置类
 * @createDate 2026/1/2 10:01
 * @since 1.0.0
 */
@Configuration
public class RestClientConfig {
    private final Logger logger = LoggerFactory.getLogger(RestClientConfig.class);

    private final RestClientPoolProperties poolProperties;

    public RestClientConfig(RestClientPoolProperties poolProperties) {
        this.poolProperties = poolProperties;
    }

    /**
     * 创建连接池, 单独声明连接池 Bean（便于监控/管理）
     *
     * @return PoolingHttpClientConnectionManager
     */
    @Bean(destroyMethod = "close")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        // 1. 配置连接池核心参数
        ConnectionConfig poolConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(poolProperties.getTimeout().getConnectTimeoutSec())) // 连接超时
                .setSocketTimeout(Timeout.ofSeconds(poolProperties.getTimeout().getSocketTimeoutSec())) // 读取超时（Socket超时）
                .setValidateAfterInactivity(Timeout.ofSeconds(poolProperties.getPool().getConnValidateAfterInactivitySec())) // 空闲连接校验时间（生产必备）
                .setTimeToLive(Timeout.ofMinutes(poolProperties.getKeepAlive().getConnTimeToLiveMin())) // 连接最大存活时长
                .build();
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(poolProperties.getPool().getMaxConnTotal()) // 最大连接数
                .setMaxConnPerRoute(poolProperties.getPool().getMaxConnPerRoute()) // 每个路由最大连接数
                .setDefaultConnectionConfig(poolConfig) // 绑定连接池配置
                .setConnPoolPolicy(PoolReusePolicy.FIFO) // 连接池获取策略
                .build();
    }

    /**
     * 创建 CloseableHttpClient Bean
     *
     * @param connectionManager PoolingHttpClientConnectionManager
     * @return CloseableHttpClient
     */
    @Bean(destroyMethod = "close")
    public CloseableHttpClient closeableHttpClient(PoolingHttpClientConnectionManager connectionManager) {
        // 1. 构建 CloseableHttpClient（5.x 标准实现类）
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .evictIdleConnections(TimeValue.ofMinutes(poolProperties.getPool().getIdleConnEvictMin())) // 清理5分钟空闲连接
                .evictExpiredConnections() // 清理过期连接（防止连接泄漏）
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofSeconds(poolProperties.getTimeout().getConnectionRequestTimeoutSec())) // 从连接池获取连接超时时间
                        .setRedirectsEnabled(true)
                        .setMaxRedirects(3)
                        .build())
                .setKeepAliveStrategy((response, context) -> Timeout.ofMinutes(poolProperties.getKeepAlive().getKeepAliveTimeoutMin()))
                .build();
    }

    /**
     * 创建 RestClient Bean
     *
     * @param httpClient CloseableHttpClient
     * @return RestClient
     */
    @Bean
    public RestClient restClient(CloseableHttpClient httpClient) {
        // 1. 构建请求工厂（适配 CloseableHttpClient）
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        // 兜底超时配置（优先级低于 HttpClient 内部配置）
        requestFactory.setConnectTimeout(Duration.ofSeconds(poolProperties.getTimeout().getConnectTimeoutSec()));
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(poolProperties.getTimeout().getConnectionRequestTimeoutSec()));

        // 3. 构建 RestClient
        return RestClient.builder()
                .requestFactory(requestFactory)
                // 请求日志拦截
                .requestInterceptor((request, body, execution) -> {
                    logger.info("RestClient 请求 URL: {}", request.getURI());
                    return execution.execute(request, body);
                })
                .build();
    }


}

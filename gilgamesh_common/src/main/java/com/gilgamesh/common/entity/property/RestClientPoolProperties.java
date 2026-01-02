package com.gilgamesh.common.entity.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description RestClient 连接池配置实体类
 * @createDate 2026/1/2 15:28
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "rest-client.pool")
public class RestClientPoolProperties {
    /**
     * 连接池核心配置
     */
    private PoolConfig pool = new PoolConfig();

    /**
     * 超时配置
     */
    private TimeoutConfig timeout = new TimeoutConfig();

    /**
     * Keep-Alive 配置
     */
    private KeepAliveConfig keepAlive = new KeepAliveConfig();

    // 内部类：连接池基础配置
    public static class PoolConfig {
        /**
         * 连接池最大总连接数
         */
        private int maxConnTotal = 100;

        /**
         * 单路由最大连接数
         */
        private int maxConnPerRoute = 50;

        /**
         * 连接等待队列长度
         */
        private int connectionWaitQueueLength = 100;

        /**
         * 空闲连接清理间隔（分钟）
         */
        private int idleConnEvictMin = 5;

        /**
         * 连接空闲超时（秒）
         */
        private int connValidateAfterInactivitySec = 30; //


        public int getMaxConnTotal() {
            return maxConnTotal;
        }

        public void setMaxConnTotal(int maxConnTotal) {
            this.maxConnTotal = maxConnTotal;
        }

        public int getMaxConnPerRoute() {
            return maxConnPerRoute;
        }

        public void setMaxConnPerRoute(int maxConnPerRoute) {
            this.maxConnPerRoute = maxConnPerRoute;
        }

        public int getConnectionWaitQueueLength() {
            return connectionWaitQueueLength;
        }

        public void setConnectionWaitQueueLength(int connectionWaitQueueLength) {
            this.connectionWaitQueueLength = connectionWaitQueueLength;
        }

        public int getIdleConnEvictMin() {
            return idleConnEvictMin;
        }

        public void setIdleConnEvictMin(int idleConnEvictMin) {
            this.idleConnEvictMin = idleConnEvictMin;
        }

        public int getConnValidateAfterInactivitySec() {
            return connValidateAfterInactivitySec;
        }

        public void setConnValidateAfterInactivitySec(int connValidateAfterInactivitySec) {
            this.connValidateAfterInactivitySec = connValidateAfterInactivitySec;
        }
    }

    // 内部类：超时配置
    public static class TimeoutConfig {

        private int connectTimeoutSec = 5; // 连接超时（秒）


        private int socketTimeoutSec = 15; // 读取超时（秒）


        private int connectionRequestTimeoutSec = 3; // 连接池获取超时（秒）

        // getter & setter
        public int getConnectTimeoutSec() {
            return connectTimeoutSec;
        }

        public void setConnectTimeoutSec(int connectTimeoutSec) {
            this.connectTimeoutSec = connectTimeoutSec;
        }

        public int getSocketTimeoutSec() {
            return socketTimeoutSec;
        }

        public void setSocketTimeoutSec(int socketTimeoutSec) {
            this.socketTimeoutSec = socketTimeoutSec;
        }

        public int getConnectionRequestTimeoutSec() {
            return connectionRequestTimeoutSec;
        }

        public void setConnectionRequestTimeoutSec(int connectionRequestTimeoutSec) {
            this.connectionRequestTimeoutSec = connectionRequestTimeoutSec;
        }
    }

    // 内部类：Keep-Alive 配置
    public static class KeepAliveConfig {

        private int connTimeToLiveMin = 30; // 连接最大存活（分钟）

        private int keepAliveTimeoutMin = 10; // Keep-Alive 超时（分钟）

        private String keepAliveHeader = "timeout=60, max=1000"; // Keep-Alive 请求头

        // getter & setter
        public int getConnTimeToLiveMin() {
            return connTimeToLiveMin;
        }

        public void setConnTimeToLiveMin(int connTimeToLiveMin) {
            this.connTimeToLiveMin = connTimeToLiveMin;
        }

        public int getKeepAliveTimeoutMin() {
            return keepAliveTimeoutMin;
        }

        public void setKeepAliveTimeoutMin(int keepAliveTimeoutMin) {
            this.keepAliveTimeoutMin = keepAliveTimeoutMin;
        }

        public String getKeepAliveHeader() {
            return keepAliveHeader;
        }

        public void setKeepAliveHeader(String keepAliveHeader) {
            this.keepAliveHeader = keepAliveHeader;
        }
    }


    public PoolConfig getPool() {
        return pool;
    }

    public void setPool(PoolConfig pool) {
        this.pool = pool;
    }

    public TimeoutConfig getTimeout() {
        return timeout;
    }

    public void setTimeout(TimeoutConfig timeout) {
        this.timeout = timeout;
    }

    public KeepAliveConfig getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(KeepAliveConfig keepAlive) {
        this.keepAlive = keepAlive;
    }
}

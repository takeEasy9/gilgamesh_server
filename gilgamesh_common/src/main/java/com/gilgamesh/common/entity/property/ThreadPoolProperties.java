package com.gilgamesh.common.entity.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 线程池核心配置实体类
 * @createDate 2025/11/24 19:57
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {
    /**
     * 核心线程数(默认5)
     */
    private int corePoolSize;

    /**
     * 最大线程数(默认2147483647)
     */
    private int maxPoolSize;

    /**
     * 任务队列容量(默认2147483647)
     */
    private int queueCapacity;

    /**
     * 空闲线程存活时间(默认60秒)
     */
    private int keepAliveSeconds;

    /**
     * 是否允许核心线程超时(默认false)
     */
    private boolean allowCoreThreadTimeout;

    /**
     * 容器关闭时是否等待任务完成(默认false)
     */
    private boolean waitForTasksToCompleteOnShutdown;

    /**
     * 等待任务完成的最大超时时间(单位：秒，默认0=无限等待)
     */
    private int awaitTerminationSeconds;


    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public boolean isAllowCoreThreadTimeout() {
        return allowCoreThreadTimeout;
    }

    public void setAllowCoreThreadTimeout(boolean allowCoreThreadTimeout) {
        this.allowCoreThreadTimeout = allowCoreThreadTimeout;
    }

    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public int getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    @Override
    public String toString() {
        return "ThreadPoolProperties{" +
                "corePoolSize=" + corePoolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", queueCapacity=" + queueCapacity +
                ", keepAliveSeconds=" + keepAliveSeconds +
                ", allowCoreThreadTimeout=" + allowCoreThreadTimeout +
                ", waitForTasksToCompleteOnShutdown=" + waitForTasksToCompleteOnShutdown +
                ", awaitTerminationSeconds=" + awaitTerminationSeconds +
                '}';
    }
}

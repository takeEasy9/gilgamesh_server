package com.gilgamesh.config;

import com.gilgamesh.common.entity.property.ThreadPoolProperties;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 线程池配置
 * @createDate 2025/11/24 19:59
 * @since 1.0.0
 */
@Configuration
public class ThreadPoolConfiguration {
    private final Logger logger = LoggerFactory.getLogger(ThreadPoolConfiguration.class);
    /**
     * 线程池配置
     */
    private final ThreadPoolProperties threadPoolProperties;

    @Autowired
    public ThreadPoolConfiguration(ThreadPoolProperties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }

    @Primary
    @Bean(name = "GilgameshPrimaryThreadPoolExecutor")
    public ThreadPoolTaskExecutor primaryThreadPoolExecutor() {
        ThreadFactory threadFactory = this.createThreadFactory("pri-pool-thread-");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadFactory(threadFactory);
        // 1. 设置核心参数
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setThreadNamePrefix("g-primary-thread-");
        executor.setAllowCoreThreadTimeOut(threadPoolProperties.isAllowCoreThreadTimeout());
        // 2. 设置拒绝策略(根据配置字符串转换为对应策略)
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池, 优雅停机核心配置, 等待任务完成后再关闭
        executor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.isWaitForTasksToCompleteOnShutdown());
        // 最大等待 30 秒，超时则强制关闭
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());
        return executor;
    }

    @Bean(name = "GilgameshSecondThreadPoolExecutor")
    public ThreadPoolTaskExecutor secondThreadPoolExecutor() {
        ThreadFactory threadFactory = this.createThreadFactory("sec-pool-thread-");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadFactory(threadFactory);
        // 1. 设置核心参数
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setThreadNamePrefix("g-second-thread-");
        executor.setAllowCoreThreadTimeOut(threadPoolProperties.isAllowCoreThreadTimeout());
        // 2. 设置拒绝策略(根据配置字符串转换为对应策略)调用者线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池, 优雅停机核心配置, 等待任务完成后再关闭
        executor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.isWaitForTasksToCompleteOnShutdown());
        // 最大等待 30 秒，超时则强制关闭
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());

        return executor;
    }

    /**
     * 线程池统一异常处理器
     * 除了这种处理方式, 还可以重写ThreadPoolTaskExecutor.afterExecute进行异常处理
     *
     * @return Thread.UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler threadUncaughtExceptionHandler() {
        return (thread, throwable) -> {
            logger.error("线程[{}]执行异常(Guava 兜底)", thread.getName(), throwable);
            // 扩展：告警、异常上报等
        };
    }

    /**
     * 创建线程工厂
     *
     * @param namePrefix 线程名称前缀
     * @return ThreadFactory
     */
    private ThreadFactory createThreadFactory(String namePrefix) {
        return new ThreadFactoryBuilder()
                .setNameFormat(namePrefix + "-%d")
                .setUncaughtExceptionHandler(threadUncaughtExceptionHandler())
                .build();
    }


}

package com.gilgamesh.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description jpa 数据源
 * @createDate 2024/10/3 20:06
 * @since 1.0.0
 */
@Configuration
public class JpaDataSourceConfiguration {
    private final Logger log = LoggerFactory.getLogger(JpaDataSourceConfiguration.class);

    @Primary
    @Bean("mysqlGilgameshDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.mysql.gilgamesh")
    public DataSource mysqlGilgameshDataSource() {
        log.info("Init mysqlGilgameshDataSource");
        return DruidDataSourceBuilder.create().build();
    }
}

package com.gilgamesh.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description mysql gilgamesh configuration
 * @createDate 2024/5/1 23:29
 * @since 1.0.0
 */

public class MySqlGilgameshConfiguration {

    @Bean("mysqlGilgameshSqlSessionFactory")
    public SqlSessionFactory mysqlGilgameshSqlSessionFactory(@Qualifier("mysqlGilgameshDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:./mapper/mysql/gilgamesh/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("mysqlGilgameshTransactionManager")
    public TransactionManager mysqlGilgameshTransactionManager(@Qualifier("mysqlGilgameshDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(value = "mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlGilgameshSqlSessionTemplate(@Qualifier("mysqlGilgameshSqlSessionFactory") SqlSessionFactory mysqlGilgameshSqlSessionFactory) {
        return new SqlSessionTemplate(mysqlGilgameshSqlSessionFactory);
    }

    @Bean("mysqlTransactionTemplate")
    public TransactionTemplate mysqlGilgameshTransactionTemplate(@Qualifier("mysqlGilgameshTransactionManager") TransactionManager transactionManager) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return new TransactionTemplate((PlatformTransactionManager) transactionManager, transactionDefinition);
    }
}

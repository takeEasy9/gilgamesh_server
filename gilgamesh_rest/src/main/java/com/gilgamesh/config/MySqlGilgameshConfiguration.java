package com.gilgamesh.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description mysql gilgamesh configuration
 * @createDate 2024/5/1 23:29
 * @since 1.0.0
 */
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "mysqlGilgameshEntityManagerFactory",
        transactionManagerRef = "mySqlGilgameshTransactionManager",
        basePackages = {"com.gilgamesh.persistence.repository.gilgamesh"})
public class MySqlGilgameshConfiguration {
    @Autowired
    private JpaProperties jpaProperties;
    @Autowired
    private HibernateProperties hibernateProperties;
    @Autowired
    @Qualifier("mysqlGilgameshDataSource")
    private DataSource mysqlGilgameshDataSource;

    @Primary
    @Bean(name = "mysqlGilgameshEntityManager")
    public EntityManager mysqlGilgameshEntityManager(EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        return Objects.requireNonNull(mysqlGilgameshEntityManagerFactory(entityManagerFactoryBuilder).getObject()).createEntityManager();
    }

    @Primary
    @Bean(name = "mysqlGilgameshEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlGilgameshEntityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        return entityManagerFactoryBuilder
                .dataSource(mysqlGilgameshDataSource)
                .properties(getProperties())
                .packages("com.gilgamesh.persistence.domain.gilgamesh")
                .persistenceUnit("mysqlGilgameshPersistenceUnit")
                .build();
    }

    private Map<String, Object> getProperties() {
        Map<String, String> map = new HashMap<>(2);
        this.jpaProperties.setProperties(map);
        return this.hibernateProperties.determineHibernateProperties(this.jpaProperties.getProperties(), new HibernateSettings());
    }

    @Primary
    @Bean(name = "mySqlGilgameshTransactionManager")
    public PlatformTransactionManager mySqlGilgameshTransactionManager(EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        return new JpaTransactionManager(Objects.requireNonNull(mysqlGilgameshEntityManagerFactory(entityManagerFactoryBuilder).getObject()));
    }
}

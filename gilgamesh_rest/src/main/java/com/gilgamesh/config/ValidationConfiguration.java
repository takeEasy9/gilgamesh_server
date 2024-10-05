package com.gilgamesh.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description validation Configuration
 * @createDate 2024/10/5 14:00
 * @since 1.0.0
 */
@Configuration
public class ValidationConfiguration {
    /**
     * 快速失败模式 Validator
     *
     * @param beanFactory AutowireCapableBeanFactory
     * @return Validator
     */
    @Primary
    @Bean("failFastValidator")
    public Validator failFastValidator(AutowireCapableBeanFactory beanFactory) {
        try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 快速失败
                .failFast(true)
                // 解决 SpringBoot 依赖注入问题
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(beanFactory))
                .buildValidatorFactory()) {
            return factory.getValidator();
        }
    }

    /**
     * 全部验证模式 Validator
     *
     * @param beanFactory AutowireCapableBeanFactory
     * @return Validator
     */
    @Bean("allValidator")
    public Validator allValidator(AutowireCapableBeanFactory beanFactory) {
        try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(false)
                // 解决 SpringBoot 依赖注入问题
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(beanFactory))
                .buildValidatorFactory()) {
            return factory.getValidator();
        }
    }
}

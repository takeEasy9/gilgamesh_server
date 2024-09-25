package com.gilgamesh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public class GilgameshRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GilgameshRestApplication.class, args);

    }
}

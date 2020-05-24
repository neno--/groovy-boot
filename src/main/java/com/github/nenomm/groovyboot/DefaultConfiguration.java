package com.github.nenomm.groovyboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Configuration
public class DefaultConfiguration {
    private static Logger log = LoggerFactory.getLogger(DefaultConfiguration.class);

    @PostConstruct
    private void sayHi() {
        log.info("Hello from default auto configuration");
    }

    @ConditionalOnMissingBean(name = {"customBeanName"})
    @Bean
    public CustomBean customBeanName() {
        log.info("Creating customBeanName");
        return new CustomBean();
    }
}

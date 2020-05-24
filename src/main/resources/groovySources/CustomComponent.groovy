package com.github.nenomm.groovyboot.components

import com.github.nenomm.groovyboot.CustomBean
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
class CustomComponent {
    private Logger log = LoggerFactory.getLogger(CustomComponent.class);

    @PostConstruct
    private void sayHi() {
        log.info("Hello from Groovy component - source!")
    }

    @Bean
    public CustomBean customBeanName() {
        log.info("Creating customBeanName - source");
        return new CustomBean();
    }
}

package com.github.nenomm.groovyboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JustAComponent {
    private Logger log = LoggerFactory.getLogger(JustAComponent.class);

    @PostConstruct
    private void sayHi() {
        log.info("oh, hai");
    }
}

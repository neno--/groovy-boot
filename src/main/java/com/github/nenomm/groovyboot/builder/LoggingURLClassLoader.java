package com.github.nenomm.groovyboot.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Enumeration;

public class LoggingURLClassLoader extends URLClassLoader {
    private static Logger log = LoggerFactory.getLogger(LoggingURLClassLoader.class);

    public LoggingURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public LoggingURLClassLoader(URL[] urls) {
        super(urls);
    }

    public LoggingURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }


    @Override
    public URL findResource(String name) {
        log.info("findResource for {}", name);
        return super.findResource(name);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        log.info("findResources for {}", name);
        return super.findResources(name);
    }

    @Override
    public URL getResource(String name) {
        log.info("getResource for {}", name);
        return super.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        log.info("getResources for {}", name);
        return super.getResources(name);
    }
}

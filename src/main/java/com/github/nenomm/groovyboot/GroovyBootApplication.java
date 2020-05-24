package com.github.nenomm.groovyboot;

import com.github.nenomm.groovyboot.builder.ClassBuilder;
import com.github.nenomm.groovyboot.builder.FileFetcher;
import org.codehaus.groovy.tools.GroovyClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.List;

@SpringBootApplication
public class GroovyBootApplication {
    private static Logger log = LoggerFactory.getLogger(GroovyBootApplication.class);

    // find classes elsewhere on fs
    // this shows that compiled groovy classes can be loaded with java classloader.
    // also this shows that spring component scanning works fine for custom made URLClassloaders.
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        List<File> files = FileFetcher.fetchSourceFiles();
        URLClassLoader cl = ClassBuilder.build(files, new File("/home/nenad/work/test/"));

        //Thread.currentThread().setContextClassLoader(cl);


        Class clazz = cl.loadClass("com.github.nenomm.groovyboot.components.CustomComponent");

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(GroovyBootApplication.class);
        springApplicationBuilder.resourceLoader(new DefaultResourceLoader(cl));
        springApplicationBuilder.run(args);
    }

    public static void main1(String[] args) throws ClassNotFoundException {
        List<File> files = FileFetcher.fetchSourceFiles();
        List<GroovyClass> groovyClasses = ClassBuilder.build(files);
        List<Class> javaClasses = ClassBuilder.convertToJavaClass(groovyClasses);


        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(GroovyBootApplication.class)
                .sources(javaClasses.toArray(new Class<?>[javaClasses.size()]));

        springApplicationBuilder.run(args);
    }
}



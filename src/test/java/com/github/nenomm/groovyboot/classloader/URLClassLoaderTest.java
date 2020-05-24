package com.github.nenomm.groovyboot.classloader;

import com.github.nenomm.groovyboot.builder.ClassBuilder;
import com.github.nenomm.groovyboot.builder.FileFetcher;
import com.github.nenomm.groovyboot.builder.LoggingURLClassLoader;
import com.github.nenomm.groovyboot.inmemory.InMemoryURLConnection;
import com.github.nenomm.groovyboot.inmemory.InMemoryURLStreamHandlerFactory;
import org.codehaus.groovy.tools.GroovyClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;

import static org.junit.Assert.*;

public class URLClassLoaderTest {
    private Logger log = LoggerFactory.getLogger(URLClassLoaderTest.class);

    @BeforeClass
    public static void setURLStreamHandlerFactory() {
        URL.setURLStreamHandlerFactory(new InMemoryURLStreamHandlerFactory());
    }

    @Test
    public void testClassLoading() throws ClassNotFoundException, MalformedURLException {
        List<File> files = FileFetcher.fetchSourceFiles();
        List<GroovyClass> classes = ClassBuilder.build(files);

        InMemoryURLConnection.store(classes);

        URLClassLoader cl = new URLClassLoader(new URL[]{new URL("inmem:/")});

        cl.loadClass("com.github.nenomm.groovyboot.components.CustomComponent");
    }

    @Test
    public void testResourceLoading() throws MalformedURLException {
        List<File> files = FileFetcher.fetchSourceFiles();
        List<GroovyClass> classes = ClassBuilder.build(files);

        InMemoryURLConnection.store(classes);

        URLClassLoader cl = new LoggingURLClassLoader(new URL[]{new URL("inmem:/")});

        assertNotNull(cl.getResource("com/"));
        assertNotNull(cl.getResource("com"));
        assertNotNull(cl.getResource("com/github/"));

        assertNull(cl.getResource("XXX12345"));

        log.info("Good one:");
        assertNotNull(cl.getResource("com/github/nenomm/groovyboot/components/CustomComponent.class"));
    }

    @Test
    public void testResourcesLoading() throws IOException {
        List<File> files = FileFetcher.fetchSourceFiles();
        List<GroovyClass> classes = ClassBuilder.build(files);

        InMemoryURLConnection.store(classes);

        URLClassLoader cl = new LoggingURLClassLoader(new URL[]{new URL("inmem:/")});

        assertNotNull(cl.getResources("com/"));
        assertNotNull(cl.getResources("com"));
        assertNotNull(cl.getResources("com/github/"));

        assertEquals(0, size(cl.getResources("XXX12345")));

        log.info("Good one:");
        assertEquals(1, size(cl.getResources("com/github/nenomm/groovyboot/components/CustomComponent.class")));
    }

    private int size(Enumeration e) {
        int count = 0;

        while (e.hasMoreElements()) {
            e.nextElement();
            count++;
        }

        return count;
    }
}

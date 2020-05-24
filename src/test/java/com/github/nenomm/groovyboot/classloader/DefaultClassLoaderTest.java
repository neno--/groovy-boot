package com.github.nenomm.groovyboot.classloader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;

import static org.junit.Assert.*;

public class DefaultClassLoaderTest {
    private Logger log = LoggerFactory.getLogger(DefaultClassLoaderTest.class);
    private ClassLoader cl = getClass().getClassLoader();

    @Test
    public void testGetResource() {
        assertNull(getClass().getClassLoader().getResource("/level0File.txt"));
        assertNotNull(getClass().getClassLoader().getResource("level0File.txt"));

        assertNotNull(cl.getResource("level1/"));
        assertNotNull(cl.getResource("level1"));
        assertNotNull(cl.getResource("level1/level1File.txt/"));
        assertNotNull(cl.getResource("level1/level1File.txt"));
    }

    @Test
    public void testGetResources() throws IOException {
        assertEquals(1, size(cl.getResources("level0File.txt")));
        assertEquals(1, size(cl.getResources("file.txt")));
        assertEquals(1, size(cl.getResources("level1/")));
        assertEquals(1, size(cl.getResources("level1////")));
    }

    @Test
    public void showPaths() {
        log.info(cl.getResource("file.txt").getPath());
        log.info(cl.getResource("file.txt/").getPath()); // this one is strange!
        log.info(cl.getResource("file.txt////").getPath()); // this one is strange!

        log.info(cl.getResource("level1").getPath());
        log.info(cl.getResource("level1/").getPath());
        log.info(cl.getResource("level1////").getPath());
    }

    @Test
    public void showFilePaths() {
        log.info(cl.getResource("file.txt").getFile());
        log.info(cl.getResource("file.txt/").getFile()); // this one is strange!
        log.info(cl.getResource("file.txt////").getFile()); // this one is strange!

        log.info(cl.getResource("level1").getFile());
        log.info(cl.getResource("level1/").getFile());
        log.info(cl.getResource("level1////").getFile());
    }

    @Test
    public void showURIs() throws URISyntaxException {
        log.info(cl.getResource("file.txt").toURI().getPath());
        log.info(cl.getResource("file.txt/").toURI().getPath()); // this one is strange!
        log.info(cl.getResource("file.txt////").toURI().getPath()); // this one is strange!

        log.info(cl.getResource("level1").toURI().getPath());
        log.info(cl.getResource("level1/").toURI().getPath());
        log.info(cl.getResource("level1////").toURI().getPath());
    }

    // this one normalizes them
    @Test
    public void showRealFilePaths() throws URISyntaxException {
        log.info(new File(cl.getResource("file.txt").toURI()).getPath());
        log.info(new File(cl.getResource("file.txt/").toURI()).getPath());
        log.info(new File(cl.getResource("file.txt////").toURI()).getPath());

        log.info(new File(cl.getResource("level1").toURI()).getPath());
        log.info(new File(cl.getResource("level1/").toURI()).getPath());
        log.info(new File(cl.getResource("level1////").toURI()).getPath());
    }

    @Test
    public void showCanonicalFilePaths() throws URISyntaxException, IOException {
        log.info(new File(cl.getResource("file.txt").toURI()).getCanonicalPath());
        log.info(new File(cl.getResource("file.txt/").toURI()).getCanonicalPath());
        log.info(new File(cl.getResource("file.txt////").toURI()).getCanonicalPath());

        log.info(new File(cl.getResource("level1").toURI()).getCanonicalPath());
        log.info(new File(cl.getResource("level1/").toURI()).getCanonicalPath());
        log.info(new File(cl.getResource("level1////").toURI()).getCanonicalPath());
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

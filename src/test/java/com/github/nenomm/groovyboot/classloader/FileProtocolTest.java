package com.github.nenomm.groovyboot.classloader;

import com.github.nenomm.groovyboot.inmemory.InMemoryURLStreamHandlerFactory;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileProtocolTest {
    private Logger log = LoggerFactory.getLogger(FileProtocolTest.class);

    @BeforeClass
    public static void setURLStreamHandlerFactory() {
        URL.setURLStreamHandlerFactory(new InMemoryURLStreamHandlerFactory());
    }

    @Test
    public void testFromRealFile() {
        URLClassLoader cl = new URLClassLoader(new URL[]{});

        URL url = cl.getResource("com/github/nenomm/groovyboot/classloader/FileProtocolTest.class");

        assertNotNull(url);
        assertEquals("file", url.getProtocol());

        log.info("File path: {}", url.getFile());
    }

    @Test
    public void testFromJimfsFile() throws IOException {
        URL url = getClass().getClassLoader().getResource("com/github/nenomm/groovyboot/classloader/FileProtocolTest.class");

        assertNotNull(url);
        assertEquals("file", url.getProtocol());

        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());

        Path path = fs.getPath("/foo");
        Files.createDirectory(path);

        Path filePath = path.resolve("FileProtocolTest.class");
        log.info("File path resolved to {}", filePath);
        Files.write(filePath, classBytes(FileProtocolTest.class));

        // unsupported operation exception
        //File f = filePath.toFile();

        assertEquals("jimfs", filePath.toUri().toURL().getProtocol());
    }

    private static byte[] classBytes(Class clazz) throws IOException {
        InputStream is = clazz.getClassLoader().getResourceAsStream(clazz.getName().replace(".", "/") + ".class");
        byte[] targetArray = new byte[is.available()];
        is.read(targetArray);

        return targetArray;
    }
}

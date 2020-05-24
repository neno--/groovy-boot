package com.github.nenomm.groovyboot.inmemory;

import org.codehaus.groovy.tools.GroovyClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryURLConnection extends URLConnection {
    private static Logger log = LoggerFactory.getLogger(InMemoryURLConnection.class);

    private static Map<URL, byte[]> contents = new HashMap<>();

    private byte[] data = null;

    protected InMemoryURLConnection(URL url) {
        super(url);
        log.info("Created in-memory URL connection.");
    }

    @Override
    public void connect() throws IOException {
        log.info("connect");
        initDataIfNeeded();
        checkDataAvailability();
        // Protected field from superclass
        super.connected = true;
    }

    @Override
    public long getContentLengthLong() {
        log.info("getContentLengthLong");
        initDataIfNeeded();
        if (data == null)
            return 0;
        return data.length;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        log.info("getInputStream");
        initDataIfNeeded();
        checkDataAvailability();
        return new ByteArrayInputStream(data);
    }

    private void initDataIfNeeded() {
        if (data == null)
            log.info("in-memory fetch for {}", super.url);
            data = contents.get(super.url);
    }

    private void checkDataAvailability() throws IOException {
        //log.info("checkDataAvailability");
        if (data == null)
            throw new IOException("In-memory data cannot be found for: " + super.url.getPath());
    }

    public static void store(GroovyClass groovyClass) throws MalformedURLException {
        URL key = new URL("inmem:/" + groovyClass.getName().replace(".", "/") + ".class");
        log.info("Storing data for key: {}", key);
        contents.put(key, groovyClass.getBytes());
    }

    public static void store(List<GroovyClass> groovyClasses) throws MalformedURLException {
        for (GroovyClass groovyClass : groovyClasses) {
            store(groovyClass);
        }
    }

}
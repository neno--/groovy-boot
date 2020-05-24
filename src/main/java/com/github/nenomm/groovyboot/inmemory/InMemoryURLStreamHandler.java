package com.github.nenomm.groovyboot.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class InMemoryURLStreamHandler extends URLStreamHandler {
    private static Logger log = LoggerFactory.getLogger(InMemoryURLStreamHandler.class);

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        log.info("Opening new in-memory URL connection...");
        return new InMemoryURLConnection(url);
    }

}
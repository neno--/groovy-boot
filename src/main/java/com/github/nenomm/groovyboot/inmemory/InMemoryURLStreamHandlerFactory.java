package com.github.nenomm.groovyboot.inmemory;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class InMemoryURLStreamHandlerFactory  implements URLStreamHandlerFactory {

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("inmem".equals(protocol)) {
            return new InMemoryURLStreamHandler();
        }

        return null;
    }

}
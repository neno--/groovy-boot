package com.github.nenomm.groovyboot.builder;

import org.codehaus.groovy.tools.GroovyClass;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class GroovyURLClassLoader extends URLClassLoader {
    public GroovyURLClassLoader(URL[] urls) {
        super(urls, GroovyURLClassLoader.class.getClassLoader());
    }

    public static GroovyURLClassLoader getNew(List<GroovyClass> groovyClasses) {
        URL[] urls = new URL[groovyClasses.size()];

        for (int i = 0; i < groovyClasses.size(); i++) {
            urls[i] = InMemoryURLFactory.build(extractPathFromClassName(groovyClasses.get(i)), groovyClasses.get(i).getBytes());
        }

        return new GroovyURLClassLoader(urls);
    }

    private static String extractPathFromClassName(GroovyClass groovyClass) {
        return groovyClass.getName().replace(".", "/") + ".class";
    }

    @Override
    public URL findResource(String name) {
        return super.findResource(name);
    }
}

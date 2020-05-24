package com.github.nenomm.groovyboot.builder;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;
import org.codehaus.groovy.tools.GroovyClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassBuilder {
    private static Logger log = LoggerFactory.getLogger(ClassBuilder.class);

    private static ClassBuilder instance;

    static {
        instance = new ClassBuilder();
    }

    private ClassBuilder() {
        // for internal use
    }

    public static List<GroovyClass> build(List<File> sources) {
        return instance.compileSources(instance.createCompilationUnit(sources, CompilerConfiguration.DEFAULT));
    }

    public static URLClassLoader build(List<File> sources, File outputDir) throws MalformedURLException {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration(CompilerConfiguration.DEFAULT);
        compilerConfiguration.setTargetDirectory(outputDir);
        compilerConfiguration.setTargetBytecode(CompilerConfiguration.JDK8);
        instance.compileSources(instance.createCompilationUnit(sources, compilerConfiguration));

        return new URLClassLoader(new URL[]{outputDir.toURI().toURL()});
    }

    private CompilationUnit createCompilationUnit(List<File> sources, CompilerConfiguration compilerConfiguration) {
        CompilationUnit compilationUnit = new CompilationUnit(compilerConfiguration);

        for (File source : sources) {
            compilationUnit.addSource(source);
        }
        return compilationUnit;
    }

    public List<GroovyClass> compileSources(CompilationUnit compilationUnit) {
        log.info("Starting compilation...");
        compilationUnit.compile(compilationUnit.getConfiguration().getTargetDirectory() == null ? Phases.CLASS_GENERATION : Phases.OUTPUT);

        if (!compilationUnit.getClasses().isEmpty()) {
            log.info("Compilation successful!");
            return compilationUnit.getClasses();
        } else {
            log.info("Nothing to compile!");
            return Collections.emptyList();
        }
    }

    public static List<Class> convertToJavaClass(List<GroovyClass> groovyClasses) throws ClassNotFoundException {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        List<Class> javaClasses = new ArrayList<>(groovyClasses.size());

        for (GroovyClass groovyClass : groovyClasses) {
            groovyClassLoader.defineClass(groovyClass.getName(), groovyClass.getBytes());
            javaClasses.add(groovyClassLoader.loadClass(groovyClass.getName()));
        }

        return javaClasses;
    }

    // todo: figure out how to dump a class to byte array
    public static URLClassLoader createForClasses(List<Class> classes, String basePath) {
        List<File> files = new ArrayList<>(classes.size());

        for (Class clazz : classes) {
            File classFile = new File(basePath + "/" + clazz.getName().replace(".", "/") + ".class");
            classFile.getParentFile().mkdirs();
            log.info("Creating new class file: {}", classFile.getPath());
            try (FileOutputStream stream = new FileOutputStream(classFile.getPath())) {
                //stream.write(clazz.get);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            files.add(classFile);
        }
        return null;
    }


}

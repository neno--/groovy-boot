package com.github.nenomm.groovyboot.builder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileFetcher {
    private static final String GROOVY_SRC_PATH = "groovySources";

    private static Logger log = LoggerFactory.getLogger(FileFetcher.class);

    private static FileFetcher instance;

    static {
        instance = new FileFetcher();
    }

    private FileFetcher() {
        // for internal use
    }

    public static List<File> fetchSourceFiles() {
        try {
            return instance.fetchFiles(GROOVY_SRC_PATH).stream().filter(file -> file.getPath().endsWith(".groovy")).collect(Collectors.toList());
        } catch (URISyntaxException e) {
            log.error("Exception while fetching files!", e);
        }

        return Collections.emptyList();
    }

    private List<File> fetchFiles(String basePath) throws URISyntaxException {
        ArrayList<File> results = new ArrayList<>();
        fetchFilesRecursively(new File(getClass().getClassLoader().getResource(basePath).toURI()), results);
        return results;
    }

    private void fetchFilesRecursively(File path, List<File> results) {
        if (path.isDirectory()) {
            log.info("Trying to fetch files from {}", path.getPath());

            for (File file : path.listFiles()) {
                fetchFilesRecursively(file, results);
            }
        } else {
            log.info("Adding file: {}", path.getPath());
            results.add(path);
        }
    }
}

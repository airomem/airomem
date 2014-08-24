/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.disk;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import pl.setblack.airomem.core.PersistenceFactory;
import pl.setblack.badass.Politician;

/**
 *
 */
public final class PersistenceDiskHelper {

    public static final String STORAGE_FOLDER = "prevayler";

    PersistenceDiskHelper() {
        throw new UnsupportedOperationException("do not create util classes");
    }

    public static String calcFolderName(final String name) {
        return STORAGE_FOLDER + "/" + name;
    }

    /**
     * Check if save of given name exists.
     */
    public static boolean exists(String name) {
        final Path path = FileSystems.getDefault().getPath(STORAGE_FOLDER, name);
        return Files.exists(path);
    }

    public static void deletePrevaylerFolder() {
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }
}

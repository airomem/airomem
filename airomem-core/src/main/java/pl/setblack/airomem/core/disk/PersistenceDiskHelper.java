/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.disk;

import org.apache.commons.io.FileUtils;
import pl.setblack.badass.Politician;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ca
 */
public final class PersistenceDiskHelper {

    public static final String STORAGE_FOLDER = "prevayler";

    PersistenceDiskHelper() {
        throw new UnsupportedOperationException("do not create util classes");
    }

    public static String calcFolderName(final Path path) {
        return path.toString();
    }

    /**
     * Check if save of given name exists.
     */
    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static void delete(Path path) {
        if (exists(path)) {
            Politician.beatAroundTheBush(() -> {
                FileUtils.deleteDirectory(path.toFile());
            });
        }
    }

    private static Path getFolderPath(String name) {
        if (name.contains(File.separator)) {
            return FileSystems.getDefault().getPath(name);
        } else {
            return calcUserPath(name);
        }
    }


    public static Path calcUserPath(String folderName) {
        final String userFolder = System.getProperty("user.home");
        return Paths.get(userFolder, STORAGE_FOLDER, folderName).toAbsolutePath();
    }

    public static Path userStoragePath() {
        final String userFolder = System.getProperty("user.home");
        return Paths.get(userFolder, STORAGE_FOLDER).toAbsolutePath();
    }
}

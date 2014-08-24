/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

import com.google.common.base.Preconditions;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;

/**
 * Simple factory for PersistenceController.
 *
 * (Better builder based solution will be created later).
 *
 * @author jarekr
 */
public class PersistenceFactory {

    public static final String STORAGE_FOLDER = PersistenceDiskHelper.STORAGE_FOLDER;

    /**
     * Init previously stored system.
     *
     * @param <T> Mutable interface of system
     * @param <R> Immutable view of system
     * @param name name of automatically created folder (to store jounal and
     * snapshots)
     * @return PersistenceController for later use
     */
    public <T extends Storable<R>, R> PersistenceController<T, R> load(String name) {
        Preconditions.checkState(exists(name));
        PersistenceController<T, R> controller = new PersistenceController<>(PersistenceDiskHelper.calcFolderName(name));
        controller.loadSystem();
        return controller;
    }

    /**
     * Init new persistent system.
     *
     * @param <T> Mutable interface of system
     * @param <R> Immutable view of system
     * @param name name of automatically created folder (to store jounal and
     * snapshots)
     * @return PersistenceController for further use
     */
    public <T extends Storable<R>, R> PersistenceController<T, R> init(String name, T initial) {
        PersistenceController<T, R> controller = new PersistenceController<>(PersistenceDiskHelper.calcFolderName(name));
        controller.initSystem(initial);
        return controller;
    }

    /**
     * Init new system or load from disk if already exists save.
     *
     * @param <T> Mutable interface of system
     * @param <R> Immutable view of system
     * @param name name of automatically created folder (to store jounal and
     * snapshots)
     * @param supplier factory creating initial state of system (if nothing was
     * saved)
     * @return PersistenceController for further use
     */
    public <T extends Storable<R>, R> PersistenceController<T, R> initOptional(String name, Supplier<T> supplier) {
        if (exists(name)) {
            return this.load(name);
        } else {
            return this.init(name, supplier.get());
        }
    }

    public boolean exists(final String name) {
        return PersistenceDiskHelper.exists(name);
    }

}

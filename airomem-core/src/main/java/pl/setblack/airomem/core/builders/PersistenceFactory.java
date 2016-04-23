/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core.builders;

import com.google.common.base.Preconditions;
import java.util.function.Supplier;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.Storable;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.airomem.core.impl.PersistenceControllerImpl;

/**
 * Simple factory for PersistenceControllerImpl.
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
     * @return PersistenceControllerImpl for later use
     */
    public <T extends Storable<R>, R> PersistenceController<T, R> load(String name) {
        Preconditions.checkState(exists(name));

        PrevaylerBuilder<T, R> builder = PrevaylerBuilder.newBuilder().withinUserFolder(name);
        return builder.build();
    }

    /**
     * Init new persistent system.
     *
     * @param <T> Mutable interface of system
     * @param <R> Immutable view of system
     * @param name name of automatically created folder (to store jounal and
     * snapshots)
     * @return PersistenceControllerImpl for further use
     */
    public <T extends Storable<R>, R> PersistenceController<T, R> init(String name, T initial) {
        PersistenceControllerImpl<T, R> controller = new PersistenceControllerImpl<>(name);
        PrevaylerBuilder<T, R> builder = PrevaylerBuilder.newBuilder().withinUserFolder(name);
        return builder.useSupplier(() -> initial).build();
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
     * @return PersistenceControllerImpl for further use
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

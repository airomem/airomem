/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Supplier;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.airomem.core.kryo.KryoSerializer;
import pl.setblack.badass.Politician;

/**
 *
 */
public class PrevaylerBuilder<T extends Storable<R>, R> {

    /**
     * Must be defined for create initialSystem.
     */
    private Supplier<T> initialSystem;

    private boolean allowOverwrite;

    private boolean allowCreate;

    private String folder;

    private boolean journalDiskSync;

    private boolean useFastJournalSerialization;

    private boolean useFastSnapshotSerialization;

    PrevaylerBuilder() {
        initialSystem = () -> {
            throw new IllegalStateException();
        };
        allowOverwrite = false;
        allowCreate = false;
        folder = "";
        journalDiskSync = false;
        useFastJournalSerialization = true;
        useFastSnapshotSerialization = false;
    }

    private PrevaylerBuilder(final PrevaylerBuilder original) {
        this.initialSystem = original.getInitialSystem();
        this.allowOverwrite = original.isAllowOverwrite();
        this.allowCreate = original.isAllowCreate();
        this.folder = original.getFolder();
        this.journalDiskSync = original.isJournalDiskSync();
        this.useFastJournalSerialization = original.isUseFastJournalSerialization();
        this.useFastSnapshotSerialization = original.isUseFastSnapshotSerialization();
    }

    public static PrevaylerBuilder newBuilder() {
        return new PrevaylerBuilder();
    }

    public PersistenceController<T, R> build() {
        PersistenceController<T, R> result = new PersistenceController<>("test");
        result.initSystem(createPrevayler(this.getInitialSystem().get()));
        return result;
    }

    Supplier<T> getInitialSystem() {
        return initialSystem;
    }

    boolean isAllowOverwrite() {
        return allowOverwrite;
    }

    boolean isAllowCreate() {
        return allowCreate;
    }

    String getFolder() {
        return folder;
    }

    boolean isJournalDiskSync() {
        return journalDiskSync;
    }

    boolean isUseFastJournalSerialization() {
        return useFastJournalSerialization;
    }

    boolean isUseFastSnapshotSerialization() {
        return useFastSnapshotSerialization;
    }

    public PrevaylerBuilder<T, R> useSupplier(final Supplier<Serializable> supplier) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.initialSystem = supplier;
        return copy;
    }

    private Prevayler createPrevayler(final Serializable system) {
        return Politician.beatAroundTheBush(() -> {
            PrevaylerFactory<Optional> factory = new PrevaylerFactory<>();
            factory.configurePrevalentSystem(Optional.of(system));
            factory.configureJournalDiskSync(false);
            factory.configurePrevalenceDirectory(PersistenceDiskHelper.calcFolderName(this.getFolder()));

            factory.configureJournalSerializer(new KryoSerializer());
            final Prevayler prev = factory.create();
            return prev;
        });

    }

    public PrevaylerBuilder<T, R> withFolder(final String folderName) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.folder = folderName;
        return copy;
    }

}

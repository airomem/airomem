/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.JavaSerializer;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.RestoreException;
import pl.setblack.airomem.core.Storable;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.airomem.core.impl.PersistenceControllerImpl;
import pl.setblack.airomem.core.impl.RoyalFoodTester;
import pl.setblack.airomem.core.kryo.KryoSerializer;

/**
 *
 */
public class PrevaylerBuilder<T extends Serializable> {

    public static final Path PREVAYLER_DEFAULT_FOLDER = Paths.get("prevayler");
    /**
     * Must be defined for create initialSystem.
     */
    private Optional<Supplier<T>> initialSystem;

    private boolean forceOverwrite;

    private boolean allowCreate;

    private Path folder;

    private boolean journalDiskSync;

    private boolean useFastJournalSerialization;

    private boolean useFastSnapshotSerialization;

    private boolean useRoyalFoodTester;

    PrevaylerBuilder() {
        initialSystem = Optional.absent();
        forceOverwrite = false;
        allowCreate = false;
        folder = PREVAYLER_DEFAULT_FOLDER;
        journalDiskSync = false;
        useFastJournalSerialization = true;
        useFastSnapshotSerialization = false;
        useRoyalFoodTester = true;
    }


    private PrevaylerBuilder(final PrevaylerBuilder<T> original) {
        this.initialSystem = original.getInitialSystem();
        this.forceOverwrite = original.isForceOverwrite();
        this.allowCreate = original.isAllowCreate();
        this.folder = original.getFolder();
        this.journalDiskSync = original.isJournalDiskSync();
        this.useFastJournalSerialization = original.isUseFastJournalSerialization();
        this.useFastSnapshotSerialization = original.isUseFastSnapshotSerialization();
        this.useRoyalFoodTester = original.isUseRoyalFoodTester();
    }

    public static <S extends Serializable> PrevaylerBuilder<S> newBuilder() {
        return new PrevaylerBuilder();
    }

    public PersistenceController<T> build() {
        PersistenceControllerImpl<T> result = new PersistenceControllerImpl<>(getFolder());
        if (this.isForceOverwrite()) {
            result.deleteFolder();
        }
        result.initSystem(createPrevayler());
        return result;
    }

    Optional<Supplier<T>> getInitialSystem() {
        return initialSystem;
    }

    boolean isForceOverwrite() {
        return forceOverwrite;
    }

    boolean isAllowCreate() {
        return allowCreate;
    }

    Path getFolder() {
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

    public boolean isUseRoyalFoodTester() {
        return useRoyalFoodTester;
    }

    public PrevaylerBuilder<T> useSupplier(final Supplier<T> supplier) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.initialSystem = Optional.of(supplier);
        return copy;
    }

    private JavaSerializer createSerializer(boolean fast) {
        if (fast) {
            return new KryoSerializer();
        } else {
            return new JavaSerializer();
        }
    }

    private Prevayler createPrevayler() {
        Preconditions.checkArgument(getInitialSystem().isPresent() || PersistenceDiskHelper.exists(this.getFolder()));
        try {
            PrevaylerFactory<RoyalFoodTester> factory = new PrevaylerFactory<>();

            if (getInitialSystem().isPresent()) {
                factory.configurePrevalentSystem(RoyalFoodTester.of(getInitialSystem().get().get(),useRoyalFoodTester));
            } else {
                factory.configurePrevalentSystem(RoyalFoodTester.absent(useRoyalFoodTester));
            }
            factory.configureJournalDiskSync(false);
            factory.configurePrevalenceDirectory(PersistenceDiskHelper.calcFolderName(this.getFolder()));

            factory.configureJournalSerializer(createSerializer(isUseFastJournalSerialization()));
            factory.configureTransactionDeepCopy(false);
            final Prevayler prev = factory.create();
            return prev;
        } catch (Error | Exception e) {
            throw new RestoreException(e);
        }

    }

    public PrevaylerBuilder<T> withFolder(final Path folder) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.folder = folder;
        return copy;
    }

    public PrevaylerBuilder<T> withinUserFolder(final String folderName) {
        final Path userPath = PersistenceDiskHelper.calcUserPath( folderName);
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.folder = userPath;
        return copy;
    }




    public PrevaylerBuilder<T> forceOverwrite(final boolean overwrite) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.forceOverwrite = overwrite;
        return copy;
    }

    public PrevaylerBuilder<T> disableRoyalFoodTester() {
        return this.withRoyalFoodTester(false);
    }

    public PrevaylerBuilder<T> withRoyalFoodTester(boolean useRFT) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.useRoyalFoodTester = useRFT;
        return copy;
    }

    public PrevaylerBuilder<T> withJournalFastSerialization(boolean fastSerialization) {
        final PrevaylerBuilder copy = new PrevaylerBuilder(this);
        copy.useFastJournalSerialization = fastSerialization;
        return copy;
    }

}

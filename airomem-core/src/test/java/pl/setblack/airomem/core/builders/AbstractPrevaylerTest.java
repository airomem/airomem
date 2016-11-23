/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import org.junit.After;
import org.junit.Before;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;

import java.io.File;
import java.io.Serializable;

/**
 * @author jarekr
 */
public abstract class AbstractPrevaylerTest<T extends Serializable> {

    protected PersistenceController<T> persistenceController;

    private String origUserHome;

    protected abstract T createSystem();

    public AbstractPrevaylerTest() {

    }

    @Before
    public void setUp() {
        File localFolder = new File("prevayler/");
        localFolder.mkdir();
        this.origUserHome = System.getProperty("user.home");
        System.setProperty("user.home", localFolder.getAbsolutePath());
        AbstractPrevaylerTest.deletePrevaylerFolder();
        this.persistenceController = PrevaylerBuilder
                .<T>newBuilder()
                .withinUserFolder("test")
                .forceOverwrite(true)
                .useSupplier(() -> createSystem())
                .build();

    }

    @After
    public void tearDown() {
        this.persistenceController.close();
        AbstractPrevaylerTest.deletePrevaylerFolder();
        System.setProperty("user.home", origUserHome);

    }

    protected void reloadController(Class<T> type) {
        this.persistenceController.close();
        this.persistenceController = PrevaylerBuilder
                .<T>newBuilder()
                .withinUserFolder("test")
                .forceOverwrite(false)
                .build();
    }

    static void deletePrevaylerFolder() {
        PersistenceDiskHelper.delete(PersistenceDiskHelper.calcUserPath("test"));
    }

}

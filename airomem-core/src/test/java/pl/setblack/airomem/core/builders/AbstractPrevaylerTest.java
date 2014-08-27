/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import pl.setblack.airomem.core.builders.PersistenceControllerImpl;
import pl.setblack.airomem.core.builders.PersistenceFactory;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.Storable;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.badass.Politician;

/**
 *
 * @author jarekr
 */
public abstract class AbstractPrevaylerTest<T extends Storable<R>, R> {

    private final PersistenceFactory factory;

    protected PersistenceController<T, R> persistenceController;

    protected abstract T createSystem();

    public AbstractPrevaylerTest() {
        factory = new PersistenceFactory();
    }

    @Before
    public void setUp() {
        AbstractPrevaylerTest.deletePrevaylerFolder();
        this.persistenceController = factory.init("test", createSystem());

    }

    @After
    public void tearDown() {
        this.persistenceController.close();
        AbstractPrevaylerTest.deletePrevaylerFolder();
    }

    protected void reloadController(Class<T> type) {
        this.persistenceController.close();
        this.persistenceController = factory.load("test");
    }

    static void deletePrevaylerFolder() {
        PersistenceDiskHelper.deletePrevaylerFolder();
    }

}

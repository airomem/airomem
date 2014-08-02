/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
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
        deletePrevaylerFolder();
        this.persistenceController = factory.init("test", createSystem());

    }

    @After
    public void tearDown() {
        this.persistenceController.close();
        deletePrevaylerFolder();
    }

    protected void reloadController(Class<T> type) {
        this.persistenceController.close();
        this.persistenceController = factory.load("test");
    }

    private void deletePrevaylerFolder() {
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

}

/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.disk;

import org.junit.Before;
import org.junit.Test;
import pl.setblack.airomem.core.builders.PersistenceFactory;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class PersistenceDiskHelperTest {

    public PersistenceDiskHelperTest() {
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldFailOnCreate() {
        new PersistenceDiskHelper();
    }

    @Test
    public void shouldTryDeleteAlsoNotExistingDir() {
        try (
        final FileOutputStream fous = new FileOutputStream(PersistenceFactory.STORAGE_FOLDER + "/test.txt");
        ) {
            fous.write("test".getBytes());
            fous.flush();
            PersistenceDiskHelper.deletePrevaylerFolder();
            PersistenceDiskHelper.deletePrevaylerFolder();
        } catch (IOException ioe) {
            //ignored
        }
    }

}

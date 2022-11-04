/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.disk;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author jarek ratajski
 */
class PersistenceDiskHelperTest {

    @Test
    void shouldFailOnCreate() {
        assertThatThrownBy(PersistenceDiskHelper::new).isInstanceOf(UnsupportedOperationException.class);

    }

    /*@Test
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
    }*/

}

/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.disk;

import org.junit.Before;
import org.junit.Test;
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

}

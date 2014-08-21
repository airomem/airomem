/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct;

import javax.ws.rs.GET;
import static org.junit.Assert.assertNotNull;

/**
 * Sample controller for tests.
 *
 */
@Persistent
public class SampleController {

    @PersistentObject
    protected SampleObject object;

    public void writeMethod() {
        assertNotNull(object);
        object.setField1("changed field1");
        object.value += 14;
    }

    @GET
    public String readMethod() {
        assertNotNull(object);
        object.value += 7;
        return object.getField1();
    }

    public SampleObject getObject() {
        return this.object;
    }

}

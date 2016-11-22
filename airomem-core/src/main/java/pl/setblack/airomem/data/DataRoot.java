/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.data;

import pl.setblack.airomem.core.Storable;

import java.io.Serializable;

/**
 * Base class for persistent (prevalent) objects.
 *
 * @author jarekr
 */
public class DataRoot< DATA >  implements Serializable{
    private static final long serialVersionId = 1l;
    private final DATA persistentObject;

    public DataRoot(DATA persistentObject) {
        this.persistentObject = persistentObject;
    }

    public DATA getDataObject() {
        return persistentObject;
    }

}

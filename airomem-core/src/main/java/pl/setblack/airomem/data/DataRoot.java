/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.data;

import pl.setblack.airomem.core.Storable;

/**
 * Base class for persistent (prevalent) objects.
 *
 * @author jarekr
 */
public class DataRoot< IMMUTABLE, DATA extends IMMUTABLE> implements Storable<IMMUTABLE> {

    private final DATA persistentObject;

    public DataRoot(DATA persistentObject) {
        this.persistentObject = persistentObject;
    }

    @Override
    public IMMUTABLE getImmutable() {
        return persistentObject;
    }

    public DATA getDataObject() {
        return persistentObject;
    }

}

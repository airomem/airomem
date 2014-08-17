/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.direct.impl;

import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.Storable;

/**
 *
 * @author jratajsk
 */
public final class PrevaylerRegister {

    private static final PrevaylerRegister INSTANCE = new PrevaylerRegister();

    public static final PrevaylerRegister getInstance() {
        return PrevaylerRegister.INSTANCE;
    }
    
    public <T extends Storable<I>,I> PersistenceController<T,I> getController(final Class<T> type, final String name) {
        return null;
    }
    
    
}

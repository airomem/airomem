/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import java.beans.Transient;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jarekr
 */
public class StorableObject implements Storable<Map<String, String>> {

    public final HashMap<String, String> internalMap;

    public transient Map<String, String> immutable;

    public StorableObject(HashMap<String, String> intenralMap) {
        this.internalMap = intenralMap;
    }

    @Override
    public synchronized Map<String, String> getImmutable() {
        if (this.immutable == null) {
            this.immutable = Collections.unmodifiableMap(internalMap);
        }
        return immutable;
    }

}

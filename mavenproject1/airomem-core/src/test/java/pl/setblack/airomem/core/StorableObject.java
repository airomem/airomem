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

    public transient final Map<String, String> immutable;

    public StorableObject(HashMap<String, String> intenralMap) {
        this.internalMap = intenralMap;
        this.immutable = Collections.unmodifiableMap(internalMap);
    }

    @Override
    public Map<String, String> getImmutable() {
        return immutable;
    }

}

/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ReferenceResolver implements com.esotericsoftware.kryo.ReferenceResolver {

    protected Kryo kryo;
    protected Map<Value, Integer> map = new HashMap<Value, Integer>();
    protected final ArrayList<Object> readObjects = new ArrayList<Object>();

    private static class Value {

        private Object val;
        //private int hash;

        public Value(Object val) {
            this.val = val;
            //this.hash =
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(val);
        }

        @Override
        public boolean equals(Object obj) {
            return val == ((Value) obj).val;
        }

    }

    public void setKryo(Kryo kryo) {
        this.kryo = kryo;
    }

    public int addWrittenObject(Object object) {
        Value v = new Value(object);
        Integer i = map.get(v);
        if (i == null) {
            int ret = map.size();
            map.put(v, ret);
            return ret;
        } else {
            return i;
        }
    }

    public int getWrittenId(Object object) {
        Value v = new Value(object);
        Integer i = map.get(v);
        if (i == null) {
            return -1;
        } else {
            return i;
        }
    }

    @SuppressWarnings("rawtypes")
    public int nextReadId(Class type) {
        int id = readObjects.size();
        readObjects.add(null);
        return id;
    }

    public void setReadObject(int id, Object object) {
        readObjects.set(id, object);
    }

    @SuppressWarnings("rawtypes")
    public Object getReadObject(Class type, int id) {
        return readObjects.get(id);
    }

    public void reset() {
        readObjects.clear();
        map.clear();
    }

    /**
     * Returns false for all primitive wrappers.
     */
    @SuppressWarnings("rawtypes")
    public boolean useReferences(Class type) {
        return !Util.isWrapperClass(type) && !type.equals(String.class) && !type.equals(Date.class) && !type.equals(BigDecimal.class) && !type.equals(BigInteger.class);
    }

    public void addReadObject(int id, Object object) {
        while (id >= readObjects.size()) {
            readObjects.add(null);
        }
        readObjects.set(id, object);
    }
}

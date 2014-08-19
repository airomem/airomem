/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import pl.setblack.airomem.core.SimpleController;

import pl.setblack.airomem.direct.PersistentObject;
import pl.setblack.badass.Politician;

/**
 *
 * @author jratajsk
 */
public class ClassContext {

    private final Class targetClass;

    private ElemHandler elem;

    private SimpleController controller;

    public ClassContext(final Object target) {
        this.targetClass = target.getClass();
    }

    private void scan() {
        List<ElemHandler> allAnootatedFields = scanFields(targetClass);
        if (allAnootatedFields.size() == 1) {
            prepare(allAnootatedFields.get(0));
        } else {
            throw new IllegalStateException("multiple stores are not supperted yet");
        }
    }

    public void runTransaction(Object target) {

    }

    public void close() {

    }

    private List<ElemHandler> scanFields(Class<? extends Object> aClass) {
        Field[] fields = aClass.getFields();
        final ArrayList<ElemHandler> result = new ArrayList<ElemHandler>(2);
        for (Field f : fields) {
            final PersistentObject po = f.getAnnotation(PersistentObject.class);
            if (po != null) {
                result.add(new FieldHandler(f));
            }
        }
        return result;
    }

    private void prepare(ElemHandler elem) {
        this.elem = elem;
        this.controller = PrevaylerRegister.getInstance().getController(elem.getTargetType(), elem.getName());
    }

    void initMutable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static interface ElemHandler {

        Class getTargetType();

        void storeValue(Object target, Object val);

        void cleanValue(Object target);

        public String getName();

    }

    private static class FieldHandler implements ElemHandler {

        private final Field field;

        private final String name;

        private final Class<?> type;

        public FieldHandler(Field field) {
            this.field = field;
            this.type = field.getType();
            this.field.setAccessible(true);
            this.name = this.field.getName();
        }

        @Override
        public void storeValue(Object target, Object val) {
            Politician.beatAroundTheBush(()
                    -> this.field.set(target, val));

        }

        @Override
        public void cleanValue(Object target) {
            Politician.beatAroundTheBush(()
                    -> this.field.set(target, null));
        }

        @Override
        public Class getTargetType() {
            return this.type;
        }

        @Override
        public String getName() {
            return this.name;
        }

    }
}

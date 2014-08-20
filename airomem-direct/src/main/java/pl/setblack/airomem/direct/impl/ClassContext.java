/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaBuilder;
import pl.setblack.airomem.core.ContextCommand;
import pl.setblack.airomem.core.PrevalanceContext;
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
        scan();
    }

    private void scan() {
        List<ElemHandler> allAnootatedFields = scanFields(targetClass);
        if (allAnootatedFields.size() == 1) {
            prepare(allAnootatedFields.get(0));
        } else {
            throw new IllegalStateException("must have exactly one PersistentObject");
        }
    }

    public void close() {

    }

    private List<ElemHandler> scanFields(Class<? extends Object> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        final ArrayList<ElemHandler> result = new ArrayList<>(2);
        for (Field f : fields) {
            final PersistentObject po = f.getAnnotation(PersistentObject.class);
            if (po != null) {
                result.add(new FieldHandler(f));
            }
        }
        if (aClass.getSuperclass() != null) {
            result.addAll(scanFields(aClass.getSuperclass()));
        }
        return result;
    }

    private void prepare(ElemHandler elem) {
        this.elem = elem;
        this.controller = PrevaylerRegister.getInstance().getController(elem.getTargetType(), elem.getName());
    }

    public Object performTransaction(final Object target, Method method) {
        final WraperTransaction transaction = new WraperTransaction(target, method.getName());
        return this.controller.executeAndQuery(transaction
        );
    }

    private void inject(Object target, Object system) {
        this.elem.storeValue(target, system);
    }

    private static class WraperTransaction implements ContextCommand<Object, Object>, Serializable {

        private static final long serialVersionUID = 1l;

        private final Object target;

        private final String methodName;

        public WraperTransaction(Object target, String methodName) {
            this.target = target;
            this.methodName = methodName;
        }

        @Override
        public Object execute(Object system, PrevalanceContext context) {
            final ClassContext ctx = new ClassContext(target);
            ctx.inject(target, system);
            return Politician.beatAroundTheBush(() -> {
                final Method method = target.getClass().getMethod(methodName);
                return method.invoke(target);
            }
            );

        }

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

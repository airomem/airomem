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
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import pl.setblack.airomem.core.ContextCommand;
import pl.setblack.airomem.core.PrevalanceContext;
import pl.setblack.airomem.core.SimpleController;
import pl.setblack.airomem.direct.OperationType;
import pl.setblack.airomem.direct.PersistenceInterceptor;
import pl.setblack.airomem.direct.PersistentObject;
import pl.setblack.badass.Politician;

/**
 *
 * @author jratajsk
 */
public class ClassContext {

    private final Class targetClass;

    private ElemHandler elem;

    ClassContext(final Class target) {
        this.targetClass = target;
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

    }

    private Class calcTargetClass(Object obj) {
        return obj.getClass().getSuperclass();
    }

    public Object performTransaction(final Object target, Method method) {

        final SimpleController controller = PrevaylerRegister.getInstance().getController(elem.getTargetType(), elem.getName());
        final OperationType opType = findRegistry().sayTypeOfMethod(method);
        if (opType == OperationType.WRITE) {
            final WraperTransaction transaction = new WraperTransaction(calcTargetClass(target).getCanonicalName(), method.getName());
            return controller.executeAndQuery(transaction
            );
        } else {
            try {
                inject(target, controller.query(immutable -> immutable));
                return Politician.beatAroundTheBush(() -> method.invoke(target));
            } finally {
                clean(target);
            }

        }
    }

    private void inject(Object target, Object system) {
        this.elem.storeValue(target, system);
    }

    private void clean(Object target) {
        this.elem.cleanValue(target);
    }

    private static BeanManager getBeanManager() {
        return Politician.beatAroundTheBush(()
                -> (BeanManager) InitialContext.doLookup("java:comp/BeanManager"));

    }

    private static Object instantiateBean(final Class cls) throws IllegalStateException {
        final BeanManager beanManager = getBeanManager();
        for (Bean<?> bean : beanManager.getBeans(cls)) {
            final CreationalContext context = beanManager.createCreationalContext(bean);
            return beanManager.getReference(bean, cls, context);
        }
        throw new IllegalStateException("no bean of class" + cls);
    }

    private static ClassContextRegistry findRegistry() {
        return (ClassContextRegistry) instantiateBean(ClassContextRegistry.class);
    }

    private static class WraperTransaction implements ContextCommand<Object, Object>, Serializable {

        private static final long serialVersionUID = 1l;

        private static final ThreadLocal<Boolean> MARKER = new ThreadLocal<>();

        private final String targetClass;

        private final String methodName;

        public WraperTransaction(String targetClass, String methodName) {
            this.targetClass = targetClass;
            this.methodName = methodName;
        }

        private Object instantiateTarget() throws ClassNotFoundException {
            final Class cls = Class.forName(this.targetClass);
            return instantiateBean(cls);
        }

        @Override
        public Object execute(Object system, PrevalanceContext context
        ) {
            PersistenceInterceptor.setMarker();
            final Object target = Politician.beatAroundTheBush(() -> instantiateTarget());
            final ClassContext ctx = findRegistry().getContext(target);
            ctx.inject(target, system);
            try {
                return Politician.beatAroundTheBush(() -> {
                    final Method method = target.getClass().getMethod(methodName);
                    return method.invoke(target);
                }
                );
            } finally {
                ctx.clean(target);
            }

        }

    }

}

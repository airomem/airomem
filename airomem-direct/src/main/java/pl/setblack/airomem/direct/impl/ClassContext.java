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
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import org.apache.commons.lang.ClassUtils;
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

    Object performTransaction(final Object target, Method method, final Object[] args) {

        final SimpleController controller = PrevaylerRegister.getInstance().getController(elem.getTargetType(), elem.getName());

        final WraperTransaction transaction = new WraperTransaction(calcTargetClass(target).getCanonicalName(), method, args);
        return controller.executeAndQuery(transaction);

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
        return (ClassContextRegistry) instantiateBean(ClassContextRegistry.class
        );
    }

    public Object performTransaction(InvocationContext ctx) {
        final Method method = ctx.getMethod();
        final OperationType opType = findRegistry().sayTypeOfMethod(method);
        if (opType == OperationType.WRITE) {
            return this.performTransaction(ctx.getTarget(), method, ctx.getParameters());
        } else {
            try {

                final SimpleController controller = PrevaylerRegister.getInstance().getController(elem.getTargetType(), elem.getName());

                inject(ctx.getTarget(), controller.query(immutable -> immutable));
                return Politician.beatAroundTheBush(() -> ctx.proceed());
            } finally {
                clean(ctx.getTarget());

            }
        }

    }

    private static class WraperTransaction implements ContextCommand<Object, Object>, Serializable {

        private static final long serialVersionUID = 1l;

        private static final ThreadLocal<Boolean> MARKER = new ThreadLocal<>();

        private final String targetClass;

        private final String methodName;

        private String[] paramClasses;

        private final Object[] arguments;

        public WraperTransaction(String targetClass, final Method method, final Object[] args) {
            this.targetClass = targetClass;
            this.methodName = method.getName();
            this.arguments = args;
            storeParams(method);
        }

        private Object instantiateTarget() throws ClassNotFoundException {
            final Class cls = Class.forName(this.targetClass);
            return instantiateBean(cls);
        }

        private Class[] getParams() {
            final Class[] result = new Class[this.paramClasses.length];
            for (int i = 0; i < result.length; i++) {
                final String className = this.paramClasses[i];
                result[i] = Politician.beatAroundTheBush(() -> ClassUtils.getClass(className));
            }
            return result;
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
                    final Method method = target.getClass().getMethod(methodName, getParams());
                    return method.invoke(target, this.arguments);
                }
                );
            } finally {
                ctx.clean(target);
            }

        }

        private void storeParams(Method method) {
            final List<String> params = new ArrayList<>(method.getParameterCount());

            for (final Class cls : method.getParameterTypes()) {
                params.add(cls.getCanonicalName());
            }

            this.paramClasses = params.toArray(new String[params.size()]);
        }

    }

}

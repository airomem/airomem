/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import pl.setblack.airomem.direct.OperationType;

/**
 *
 */
@ApplicationScoped
public class ClassContextRegistry {

    private final Map<Class, ClassContext> cache;

    @Inject
    private TransactionAnalyzer transactionAnalyzer;

    public ClassContextRegistry() {
        this.cache = new HashMap<>();
    }

    public synchronized ClassContext getContext(final Object object) {
        final Class clz = object.getClass();
        final ClassContext ctx = this.cache.get(clz);
        if (ctx == null) {
            return createAndStoreNewContext(clz);
        }
        return ctx;
    }

    private ClassContext createAndStoreNewContext(Class clz) {
        final ClassContext ctx = new ClassContext(clz);
        this.cache.put(clz, ctx);
        return ctx;
    }

    public OperationType sayTypeOfMethod(final Method method) {
        return this.transactionAnalyzer.sayTypeOf(method);
    }
}

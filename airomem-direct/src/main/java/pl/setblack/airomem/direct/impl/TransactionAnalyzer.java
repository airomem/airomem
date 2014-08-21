/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.enterprise.context.ApplicationScoped;
import javax.swing.text.html.FormSubmitEvent;
import pl.setblack.airomem.direct.OperationType;

/**
 * Analyzes transaction kind.
 */
@ApplicationScoped
public class TransactionAnalyzer {

    private Class[] WRITE_ANNOTATIONS = {};

    private Class[] READ_FAST_ANNOTATIONS = {javax.ws.rs.GET.class};

    public OperationType sayTypeOf(final Method method) {
        final Annotation[] annotations = method.getAnnotations();
        if (contains(annotations, READ_FAST_ANNOTATIONS)) {
            return OperationType.READ_FAST;
        }
        return OperationType.WRITE;
    }

    private boolean contains(Annotation[] annotations, Class[] set) {
        for (final Class clz : set) {
            for (final Annotation ann : annotations) {
                if (clz == ann.annotationType()) {
                    return true;
                }
            }
        }
        return false;
    }
}

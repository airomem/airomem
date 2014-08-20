/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.lang.reflect.Field;
import pl.setblack.badass.Politician;

/**
 *
 */
class FieldHandler implements ElemHandler {

    final Field field;
    final String name;
    final Class<?> type;

    public FieldHandler(Field field) {
        this.field = field;
        this.type = field.getType();
        this.field.setAccessible(true);
        this.name = this.field.getName();
    }

    @Override
    public void storeValue(Object target, Object val) {
        Politician.beatAroundTheBush(() -> this.field.set(target, val));
    }

    @Override
    public void cleanValue(Object target) {
        Politician.beatAroundTheBush(() -> this.field.set(target, null));
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

/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import pl.setblack.airomem.core.SimpleController;
import pl.setblack.badass.Politician;

/**
 *
 * @author jratajsk
 */
public final class PrevaylerRegister {

    private static final PrevaylerRegister INSTANCE = new PrevaylerRegister();

    private final ConcurrentHashMap<String, SimpleController> prevaylers = new ConcurrentHashMap<>();

    public static final PrevaylerRegister getInstance() {
        return PrevaylerRegister.INSTANCE;
    }

    public void clear() {
        for (final SimpleController ctrl : this.prevaylers.values()) {
            ctrl.close();
        }
        this.prevaylers.clear();
    }

    public <T extends Serializable> SimpleController<T> getController(final Class<T> type, final String name) {
        final String fullName = calcName(type, name);
        final SimpleController<T> existing = this.prevaylers.get(fullName);
        if (existing != null) {
            return existing;
        } else {
            final SimpleController<T> newPrevayler = createController(type, name);
            this.prevaylers.putIfAbsent(fullName, newPrevayler);
            return this.prevaylers.get(fullName);
        }

    }

    private <T extends Serializable> SimpleController<T> createController(Class<T> type, String name) {
        SimpleController<T> controller = SimpleController.loadOptional(name, () -> createInitialState(type));
        return controller;
    }

    private <T extends Serializable> T createInitialState(Class<T> type) {
        return Politician.beatAroundTheBush(() -> {
            final Constructor<T> constr = type.getDeclaredConstructor();
            return constr.newInstance();
        });
    }

    private static <T> String calcName(final Class<T> clazz, final String name) {
        return clazz.getCanonicalName() + name;
    }

    public void shut() {
        for (final SimpleController ctrl : this.prevaylers.values()) {
            ctrl.shut();
        }
        this.prevaylers.clear();
    }
}

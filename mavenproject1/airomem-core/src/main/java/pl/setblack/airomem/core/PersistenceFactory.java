/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

/**
 *
 * @author jarekr
 */
public class PersistenceFactory {

    public <T extends Storable<R>, R> PersistenceController<T, R> load(String name, Class<T> type) {
        PersistenceController<T, R> controller = new PersistenceController<>(name);
        controller.loadSystem();
        return controller;
    }

    public <T extends Storable<R>, R> PersistenceController<T, R> init(String name, T initial) {
        PersistenceController<T, R> controller = new PersistenceController<>(name);
        controller.initSystem(initial);
        return controller;

    }
}

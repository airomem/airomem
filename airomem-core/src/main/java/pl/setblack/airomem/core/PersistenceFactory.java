/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

/**
 *
 * @author jarekr
 */
public class PersistenceFactory {

    public static final String STORAGE_FOLDER = "prevayler";

    public <T extends Storable<R>, R> PersistenceController<T, R> load(String name, Class<T> type) {
        PersistenceController<T, R> controller = new PersistenceController<>(calcFolderName(name));
        controller.loadSystem();
        return controller;
    }

    public <T extends Storable<R>, R> PersistenceController<T, R> init(String name, T initial) {
        PersistenceController<T, R> controller = new PersistenceController<>(calcFolderName(name));
        controller.initSystem(initial);
        return controller;

    }

    private String calcFolderName(final String name) {
        return STORAGE_FOLDER + "/" + name;
    }
}

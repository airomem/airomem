/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import java.io.IOException;
import java.io.Serializable;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

/**
 *
 * @author jarekr
 */
public class PersistenceController<T extends Storable<IMMUTABLE>, IMMUTABLE> {

    private Prevayler<T> prevayler;

    private final String uniqueName;

    PersistenceController(String name) {
        this.uniqueName = name;
    }

    public void close() {
        try {
            this.prevayler.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    void initSystem(T object) {
        this.prevayler = createPrevayler(object);
    }

    public <RESULT> RESULT query(Query<IMMUTABLE, RESULT> query) {
        return query.evaluate(getImmutable());
    }

    private T getObject() {
        return this.prevayler.prevalentSystem();
    }

    private IMMUTABLE getImmutable() {
        return this.getObject().getImmutable();
    }

    private Prevayler createPrevayler(final Serializable system) {
        try {
            final Prevayler prev = PrevaylerFactory.createPrevayler(
                    system, this.uniqueName);

            return prev;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    void loadSystem() {
        this.prevayler = createPrevayler("Not needed");
    }

}

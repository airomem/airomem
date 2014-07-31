/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
import java.io.Serializable;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import pl.setblack.airomem.core.kryo.KryoSerializer;
import pl.setblack.badass.Politician;

/**
 *
 * @author jarekr
 */
public class PersistenceController<T extends Storable<IMMUTABLE>, IMMUTABLE> {

    private Prevayler<Optional<T>> prevayler;

    private final String uniqueName;

    PersistenceController(String name) {
        this.uniqueName = name;
    }

    public void close() {
        Politician.beatAroundTheBush(() -> {
            this.prevayler.takeSnapshot();
            this.prevayler.close();
            this.prevayler = null;
        });
    }

    public void shut() {
        Politician.beatAroundTheBush(() -> {
            this.prevayler.close();
            this.prevayler = null;
        });
    }

    void initSystem(T object) {
        this.prevayler = createPrevayler(object);
        Politician.beatAroundTheBush(() -> this.prevayler.takeSnapshot());
    }

    public <RESULT> RESULT query(Query<IMMUTABLE, RESULT> query) {
        return query.evaluate(getImmutable());
    }

    public void execute(ContextCommand<T> cmd) {
        Politician.beatAroundTheBush(() -> this.prevayler.execute(new InternalTransaction<>(cmd)));
    }

    public void execute(Command<T> cmd) {
        this.execute((ContextCommand<T>) cmd);
    }

    private T getObject() {
        return this.prevayler.prevalentSystem().get();
    }

    private IMMUTABLE getImmutable() {
        return this.getObject().getImmutable();
    }

    private Prevayler createPrevayler(final Serializable system) {
        return Politician.beatAroundTheBush(() -> {
            PrevaylerFactory<Optional> factory = new PrevaylerFactory<>();
            factory.configurePrevalentSystem(Optional.of(system));
            factory.configureJournalDiskSync(false);
            factory.configurePrevalenceDirectory(this.uniqueName);

            factory.configureJournalSerializer(new KryoSerializer());
            final Prevayler prev = factory.create();
            return prev;
        });

    }

    void loadSystem() {
        this.prevayler = createPrevayler(Optional.absent());
    }

}

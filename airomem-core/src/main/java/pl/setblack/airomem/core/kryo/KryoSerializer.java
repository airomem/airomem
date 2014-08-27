/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.prevayler.foundation.serialization.JavaSerializer;

/**
 *
 * @author jarekr
 */
public class KryoSerializer extends JavaSerializer {

    private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = pool.borrow();
            return kryo;
        }
    ;
    };


    private final KryoPool pool = new KryoPool(KryoSerializer::createCryo);

    private static Kryo createCryo() {
        final Kryo kryo = new Kryo();
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.setReferenceResolver(new ReferenceResolver());
        return kryo;
    }

    public KryoSerializer() {
        //kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        // kryo.register(ContextCommand.class, new ClosureSerializer());
        //  kryo.register(Command.class, new ClosureSerializer());
    }

    private Kryo getKryo() {
        return this.kryos.get();
    }

    @Override
    public Object readObject(InputStream stream) throws IOException, ClassNotFoundException {
        try (Input input = new Input(stream)) {
            return getKryo().readClassAndObject(input);
        }

    }

    @Override
    public void writeObject(OutputStream stream, Object object) throws IOException {
        try (Output output = new Output(stream)) {
            getKryo().writeClassAndObject(output, object);
        }
    }

}

/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.prevayler.foundation.serialization.JavaSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

/**
 * @author jarekr
 */
public class KryoSerializer extends JavaSerializer {
    private Supplier<Kryo> factory = () -> {
        final Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        //kryo.register(java.lang.invoke.SerializedLambda.class);
        try {
            kryo.register(Class.forName(Kryo.class.getName() + "$Closure"), new ClosureSerializer());
        } catch (ClassNotFoundException e) {
        }
        kryo.setReferenceResolver(new ReferenceResolver());
        return kryo;
    };
    private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            return factory.get();
        }
    };



//    private final KryoPool pool = new KryoPool.Builder(factory).build();

    private Kryo getKryo() {
        return this.kryos.get();
    }

    @Override
    public Object readObject(InputStream stream) throws IOException, ClassNotFoundException {
        try (Input input = new Input(stream, 1024)) {
            return getKryo().readClassAndObject(input);
        }
    }

    @Override
    public void writeObject(OutputStream stream, Object object) throws IOException {
        try (Output output = new Output(stream, 1024)) {
            getKryo().writeClassAndObject(output, object);
        }
    }

}

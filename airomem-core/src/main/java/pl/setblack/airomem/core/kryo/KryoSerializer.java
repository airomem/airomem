/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.prevayler.foundation.serialization.JavaSerializer;
import pl.setblack.airomem.core.Command;
import pl.setblack.airomem.core.ContextCommand;

/**
 *
 * @author jarekr
 */
public class KryoSerializer extends JavaSerializer {

    private final Kryo kryo = new Kryo();

    public KryoSerializer() {
        //kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(ContextCommand.class, new ClosureSerializer());
        kryo.register(Command.class, new ClosureSerializer());
    }

    @Override
    public Object readObject(InputStream stream) throws IOException, ClassNotFoundException {
        try (Input input = new UnsafeInput(stream)) {
            return kryo.readClassAndObject(input);
        }

    }

    @Override
    public void writeObject(OutputStream stream, Object object) throws IOException {
        try (Output output = new UnsafeOutput(stream)) {
            kryo.writeClassAndObject(output, object);
        }
    }

}

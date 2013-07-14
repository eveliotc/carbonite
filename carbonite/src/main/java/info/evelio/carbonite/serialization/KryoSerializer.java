package info.evelio.carbonite.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import info.evelio.carbonite.util.Util;

import java.io.InputStream;
import java.io.OutputStream;

import static info.evelio.carbonite.util.Util.notNullArg;

public class KryoSerializer<T> implements Serializer<T> {
  private final Kryo mKryo;
  private final Class<T> mType;

  public KryoSerializer(Kryo kryo, Class<T> type) {
    notNullArg(kryo, "You must provide a valid kryo instance.");
    notNullArg(type, "You must provide a valid class.");

    mKryo = kryo;
    mType = type;
  }

  @Override
  public T read(InputStream in) {
    Input input = null;
    try {
      input = new Input(in);
      return mKryo.readObjectOrNull(input, mType);
    } finally {
      Util.closeSilently(input);
      Util.closeSilently(in);
    }
  }

  @Override
  public boolean write(OutputStream out, T value) {
    Output output = null;
    try {
      output = new Output(out);
      mKryo.writeObjectOrNull(output, value, mType);
      return true;
    } finally {
      Util.closeSilently(output);
      Util.closeSilently(out);
    }
  }
}

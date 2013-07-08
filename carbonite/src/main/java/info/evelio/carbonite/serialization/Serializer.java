package info.evelio.carbonite.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer<T> {
  public T read(InputStream in);
  public boolean write(OutputStream out, T value);
}

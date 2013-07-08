package info.evelio.carbonite.cache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;
import info.evelio.carbonite.L;
import info.evelio.carbonite.serialization.Serializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static info.evelio.carbonite.Util.notNullArg;
import static info.evelio.carbonite.Util.validateKey;

/**
 * Best effort storage cache to use a LRU cache in a directory and a {@link Serializer} to put values on it.
 * @param <T>
 */
public class StorageLruCache<T> implements Cache<String, T> {
  private static final int MINIMAL_CAPACITY = 1048576;
  public static final String TAG = "carbonite:StorageLruCache";
  private static final int VALUE_INDEX = 0;

  private final File mDir;
  private final int mCapacity;
  private final Class<T> mType;
  private final Serializer<T> mSerializer;
  private DiskLruCache mCache;

  public StorageLruCache(File file, int capacity, Class type, Serializer<T> serializer) {
    notNullArg(file, "Cache directory must not be null.");
    notNullArg(type, "Class must not be null.");
    notNullArg(serializer, "Serializer must not be null.");

    mDir = file;
    mType = type;
    mSerializer = serializer;
    if (capacity < MINIMAL_CAPACITY) {
      capacity = MINIMAL_CAPACITY;
    }
    mCapacity = capacity;
  }

  @Override
  public T get(String key) {
    validateKey(key);
    openCache();
    if (mCache == null) {
      return null;
    }

    Snapshot snapshot = null;
    try {
      snapshot = mCache.get(key);
    } catch (IOException e) {
      L.e(TAG, "Unable to get " + key + " for " + mType, e);
    }

    if (snapshot == null) {
      return null;
    }

    final InputStream in = snapshot.getInputStream(VALUE_INDEX);
    return mSerializer.read(in);
  }

  @Override
  public StorageLruCache<T> set(String key, T value) {
    if (value == null) {
      return this;
    }

    validateKey(key);
    openCache();
    if (mCache == null) {
      return this;
    }

    Editor editor = null;
    try {
      editor = mCache.edit(key);
    } catch (IOException e) {
      L.e(TAG, String.format("Unable to edit %s for %s of type %s ", key, value,mType), e);
    }

    if (editor == null) {
      return this;
    }

    try {
      final OutputStream out = editor.newOutputStream(VALUE_INDEX);
      final boolean success = mSerializer.write(out, value);
      if (!success) {
        throw new IOException("Serializer failed to write.");
      }
    } catch (IOException e) {
      e(e, "Unable to set key %s to %s of type %s ", key, value, mType);
      try {
        editor.abort();
      } catch (IOException eAbort) {
        e(eAbort, "Unable to abort editor on key %s for %s of type %s ", key, value, mType);
      }
    }

    return this;
  }

  private void openCache() {
    if (mCache != null) {
      return;
    }

    try {
      mCache = DiskLruCache.open(mDir, 1, 1, mCapacity);
    } catch (IOException e) {
      final String msg = "Failure to open DiskLruCache for type " + mType;
      L.e(TAG, msg, e);
    }
  }

  private static void e(Throwable th, String tmpl, Object... args) {
    L.e(TAG, String.format(tmpl, args), th);
  }

}

package info.evelio.carbonite.util;

import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.CacheOptions;
import info.evelio.carbonite.cache.CacheType;
import info.evelio.carbonite.cache.MemoryLruCache;
import info.evelio.carbonite.cache.ReferenceCache;
import info.evelio.carbonite.cache.SortedReferenceCache;
import info.evelio.carbonite.cache.StorageLruCache;
import info.evelio.carbonite.cache.WeakKeyCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static info.evelio.carbonite.util.TestHelper.generateStorageLruCacheOpts;
import static info.evelio.carbonite.util.Util.newCacheInstance;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CacheInstantationTest {
  private static final Map<Class<? extends Cache>, CacheOptions<? extends Cache>> IMPS =
      new HashMap<Class<? extends Cache>, CacheOptions<? extends Cache>>();

  private static final Set<Map.Entry<Class<? extends Cache>,CacheOptions<? extends Cache>>> ENTRIES;
  static {
    IMPS.put(ReferenceCache.class, new ReferenceCache.Options(0, 0.75f));
    IMPS.put(SortedReferenceCache.class, new SortedReferenceCache.Options(null));

    IMPS.put(StorageLruCache.class, generateStorageLruCacheOpts());
    IMPS.put(MemoryLruCache.class, new MemoryLruCache.Options(1));
    IMPS.put(WeakKeyCache.class, new WeakKeyCache.Options(0, 0.75f));

    ENTRIES = IMPS.entrySet();
  }

  @Test
  public void testCanBeInstantiatedWithProperOptions() {
    for (Map.Entry<Class<? extends Cache>,CacheOptions<? extends Cache>> entry : ENTRIES) {
      final Cache<Object,Object> cache = newCacheInstance(entry.getKey(), entry.getValue());
      assertThat(cache).isNotNull();
    }
  }

  @Test
  public void testCannotBeInstantiatedWithWrongOptions() {
    assertInvalidWith(null);
    assertInvalidWith(new Object());
    assertInvalidWith(new NoOpCacheOptions());
  }

  private void assertInvalidWith(Object o) {
    for (Map.Entry<Class<? extends Cache>,CacheOptions<? extends Cache>> entry : ENTRIES) {
      try {
        final Cache<Object,Object> cache = newCacheInstance(entry.getKey(), null);
        assertThat(cache).isNull();
      } catch (Exception e) {
        assertThat(e).isNotInstanceOf(ClassCastException.class);
        e.printStackTrace();
      }
    }
  }

  private static class NoOpCacheOptions implements CacheOptions {
    @Override
    public CacheType cacheType() {
      return null;
    }

    @Override
    public Class<?> imp() {
      return null;
    }
  };

}

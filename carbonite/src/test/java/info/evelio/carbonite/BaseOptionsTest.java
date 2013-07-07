package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.ReferenceCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.Carbonite.CacheType.STORAGE;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BaseOptionsTest {
  private final Context mContext = Robolectric.application;

  @Test(expected = Exception.class) public void testNullBuilderThrows() {
    new BaseOptions(null, Object.class);
  }

  @Test(expected = Exception.class) public void testNullClassThrows() {
    new BaseOptions(mockBuilder(), null);
  }

  @Test public void testNotNull() {
    BaseOptions opts = new BaseOptions( mockBuilder(), Object.class );
  }

  @Test public void testDefaultValues() {
    BaseOptions opts = new BaseOptions( mockBuilder(), Object.class );

    assertThat(opts.factory()).isNotNull();
    assertThat(opts.in()).isInstanceOf(CacheType.class);
    assertThat(opts.capacity()).isGreaterThanOrEqualTo(0);
    assertThat(opts.loadFactor()).isPositive();
    assertThat(opts.builder()).isNotNull();
    assertThat(opts.nullValues()).isNotNull();
    assertThat(opts.imp()).isNull();
  }

  @Test public void testChaining() {
    final CacheFactory factory = mockFactory();
    final CarboniteBuilder builder = mockBuilder();

    final CarboniteBuilder.Options opts = new BaseOptions(builder, Object.class)
        .in(STORAGE)
        .nullValues(true)
        .capacity(12)
        .loadFactor(2.0f)
        .imp(ReferenceCache.class)
        .factory(factory);

    assertThat(opts.builder()).isEqualTo(builder);
    assertThat(opts.in()).isEqualTo(STORAGE);
    assertThat(opts.nullValues()).isTrue();
    assertThat(opts.capacity()).isEqualTo(12);
    assertThat(opts.loadFactor()).isEqualTo(2.0f);
    // Couldn't find something to compare as most of classes will fail to compile
    assertThat(opts.imp() == ReferenceCache.class).isTrue();
    assertThat(opts.factory()).isEqualTo(factory);
  }

  private CacheFactory mockFactory() {
    return mock(CacheFactory.class);
  }

  private CarboniteBuilder mockBuilder() {
    CarboniteBuilder mock = mock(CarboniteBuilder.class);
    when(mock.context()).thenReturn(mContext);
    return mock;
  }

}

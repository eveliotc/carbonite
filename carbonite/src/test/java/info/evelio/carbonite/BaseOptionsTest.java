package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.CacheFactory;
import info.evelio.carbonite.cache.ReferenceCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.Carbonite.CacheType.STORAGE;
import static info.evelio.carbonite.CarboniteBuilder.Options;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BaseOptionsTest {
  private final Class<? extends Object> CLASS_TO_RETAIN = Object.class;
  private final Context mContext = Robolectric.application;

  @Test(expected = Exception.class) public void testNullBuilderThrows() {
    new BaseOptions(null, CLASS_TO_RETAIN);
  }

  @Test(expected = Exception.class) public void testNullClassThrows() {
    new BaseOptions(mockBuilder(), null);
  }

  @Test public void testNotNull() {
    BaseOptions opts = new BaseOptions( mockBuilder(), CLASS_TO_RETAIN );
  }

  @Test public void testDefaultValues() {
    BaseOptions opts = new BaseOptions( mockBuilder(), CLASS_TO_RETAIN );

    assertThat(opts.factory()).isNotNull();
    assertThat(opts.in()).isInstanceOf(CacheType.class);
    assertThat(opts.capacity()).isGreaterThanOrEqualTo(0);
    assertThat(opts.loadFactor()).isPositive();
    assertThat(opts.builder()).isNotNull();
    assertThat(opts.imp()).isNull();
  }

  @Test public void testBaseChaining() {
    final CacheFactory factory = mockFactory();
    final CarboniteBuilder builder = mockBuilder();

    final Options opts = new BaseOptions(builder, CLASS_TO_RETAIN)
        .in(STORAGE)
        .capacity(12)
        .loadFactor(2.0f)
        .imp(ReferenceCache.class)
        .factory(factory);

    assertThat(opts.builder()).isEqualTo(builder);
    assertThat(opts.in()).isEqualTo(STORAGE);
    assertThat(opts.capacity()).isEqualTo(12);
    assertThat(opts.loadFactor()).isEqualTo(2.0f);
    // Couldn't find something to compare as most of classes will fail to compile
    assertThat(opts.imp() == ReferenceCache.class).isTrue();
    assertThat(opts.factory()).isEqualTo(factory);
  }

  @Test public void andSpawnsNewOptionsAndSameRetaining() {
    final Options opts = new BaseOptions( mockBuilder(), CLASS_TO_RETAIN );
    for (CacheType type : CacheType.values()) {
      final Options anotherOptions = opts.and(type);
      assertThat(opts).isNotEqualTo( anotherOptions );
      assertThat(opts.retaining() == anotherOptions.retaining()).isTrue();
    }
  }

  @Test public void wrapingMethodsWillWrap() {
    final ConfirmCallCarboniteBuilder builder = new ConfirmCallCarboniteBuilder();
    final Options opts = new BaseOptions( builder, CLASS_TO_RETAIN );

    assertThat(opts.builder()).isEqualTo(builder);

    builder.reset();
    opts.context();
    builder.assertItWasCalled();

    builder.reset();
    opts.retaining(CLASS_TO_RETAIN);
    builder.assertItWasCalled();

    builder.reset();
    opts.iLoveYou();
    builder.assertItWasCalled();

    builder.reset();
    opts.iKnow();
    builder.assertItWasCalled();

    builder.reset();
    opts.build();
    builder.assertItWasCalled();
  }

  private CacheFactory mockFactory() {
    return mock(CacheFactory.class);
  }

  private CarboniteBuilder mockBuilder() {
    CarboniteBuilder mock = mock(CarboniteBuilder.class);
    when(mock.context()).thenReturn(mContext);
    when(mock.retaining(CLASS_TO_RETAIN)).thenReturn(new BaseOptions(mock, CLASS_TO_RETAIN));
    return mock;
  }

}

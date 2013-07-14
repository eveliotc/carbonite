/*
 * Copyright 2013 Evelio Tarazona Cáceres <evelio@evelio.info>
 * Copyright 2013 Carbonite contributors <contributors@evelio.info>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.CacheFactory;
import info.evelio.carbonite.cache.CacheType;
import info.evelio.carbonite.cache.ReferenceCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static info.evelio.carbonite.CarboniteApi.CacheBuilder;
import static info.evelio.carbonite.CarboniteApi.CarboniteBuilder;
import static info.evelio.carbonite.CarboniteBuilderBaseImp.DefaultCacheBuilder;
import static info.evelio.carbonite.cache.CacheType.MEMORY;
import static info.evelio.carbonite.cache.ReferenceCache.Options;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DefaultCacheBuilderTest {
  private final Class<? extends Object> CLASS_TO_RETAIN = Object.class;
  private final Context mContext = Robolectric.application;

  @Test(expected = Exception.class) public void testNullBuilderThrows() {
    new DefaultCacheBuilder(null, CLASS_TO_RETAIN);
  }

  @Test(expected = Exception.class) public void testNullClassThrows() {
    new DefaultCacheBuilder(mockBuilder(), null);
  }

  @Test public void testNotNull() {
    DefaultCacheBuilder opts = new DefaultCacheBuilder( mockBuilder(), CLASS_TO_RETAIN );
  }

  @Test public void testDefaultValues() {
    DefaultCacheBuilder opts = new DefaultCacheBuilder( mockBuilder(), CLASS_TO_RETAIN );

    assertThat(opts.factory()).isNotNull();
    assertThat(opts.cacheType()).isInstanceOf(CacheType.class);
    assertThat(opts.builder()).isNotNull();
    assertThat(opts.type()).isEqualTo(CLASS_TO_RETAIN);
    assertThat(opts.context()).isNotNull();
  }

  @Test public void testBaseChaining() {
    final CacheFactory factory = mockFactory();
    final CarboniteBuilder builder = mockBuilder();

    final Options opts = new Options(1, 1);
    final CacheBuilder cacheBuilder = new DefaultCacheBuilder(builder, CLASS_TO_RETAIN)
        .in(MEMORY)
        .use(opts)
        .use(factory);

    assertThat(cacheBuilder.builder()).isEqualTo(builder);
    assertThat(cacheBuilder.cacheType()).isEqualTo(MEMORY);
    assertThat(cacheBuilder.opts()).isEqualTo(opts);
    // Couldn't find something to compare as most of classes will fail to compile
    assertThat(cacheBuilder.opts().imp() == ReferenceCache.class).isTrue();
    assertThat(cacheBuilder.factory()).isEqualTo(factory);
  }

  @Test public void andSpawnsNewOptionsAndSameRetaining() {
    final CacheBuilder opts = new DefaultCacheBuilder( mockBuilder(), CLASS_TO_RETAIN );
    for (CacheType type : CacheType.values()) {
      final CacheBuilder anotherOptions = opts.and(type);
      assertThat(opts).isNotEqualTo( anotherOptions );
      assertThat(opts.type() == anotherOptions.type()).isTrue();
    }
  }

  @Test public void wrapingMethodsWillWrap() {
    final ConfirmCallCarboniteBuilder builder = new ConfirmCallCarboniteBuilder();
    final CacheBuilder opts = new DefaultCacheBuilder( builder, CLASS_TO_RETAIN );

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
    when(mock.retaining(CLASS_TO_RETAIN)).thenReturn(new DefaultCacheBuilder(mock, CLASS_TO_RETAIN));
    return mock;
  }

}

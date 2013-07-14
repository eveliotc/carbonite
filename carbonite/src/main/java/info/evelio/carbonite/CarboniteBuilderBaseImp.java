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
import info.evelio.carbonite.cache.CacheOptions;
import info.evelio.carbonite.cache.CacheType;

import static info.evelio.carbonite.Carbonite.Defaults.FACTORY;
import static info.evelio.carbonite.Carbonite.Defaults.TYPE;
import static info.evelio.carbonite.CarboniteApi.CacheBuilder;
import static info.evelio.carbonite.CarboniteApi.CarboniteBuilder;
import static info.evelio.carbonite.util.Util.illegalArg;
import static info.evelio.carbonite.util.Util.notNullArg;

/*package*/ abstract class CarboniteBuilderBaseImp implements CarboniteBuilder {

  private final Context mContext;

  protected CarboniteBuilderBaseImp(Context applicationContext) {
    notNullArg(applicationContext, "Context must not be null.");

    mContext = applicationContext;
  }

  @Override
  public final Context context() {
    return mContext;
  }

  @Override
  public final CarboniteBuilder iLoveYou() {
    return this;
  }

  @Override
  public final Carbonite iKnow() {
    return build();
  }

  protected static class DefaultCacheBuilder implements CacheBuilder {
    private final CarboniteBuilder mBuilder;
    private final Class mValueType;

    private CacheType mIn = TYPE;
    private CacheFactory mFactory = FACTORY;

    private CacheOptions mOpts;

    public DefaultCacheBuilder(CarboniteBuilder carboniteBuilder, Class retainingType) {
      notNullArg(carboniteBuilder, "Builder must not be null.");
      notNullArg(retainingType, "Retaining type must not be null.");

      mBuilder = carboniteBuilder;
      mValueType = retainingType;
    }

    @Override
    public CacheBuilder in(CacheType type) {
      notNullArg(type, "CacheType in must not be null.");

      mIn = type;

      return this;
    }

    @Override
    public CacheType cacheType() {
      return mIn;
    }

    @Override
    public CacheBuilder use(CacheFactory factory) {
      notNullArg(factory, "You must provide a valid factory.");

      mFactory = factory;

      return this;
    }

    @Override
    public CacheFactory factory() {
      return mFactory;
    }

    @Override
    public CacheBuilder use(CacheOptions opts) {
      notNullArg(opts, "You must provide valid options.");
      notNullArg(opts.cacheType(), "You must provide valid options with proper cache type (cacheType).");
      illegalArg(opts.cacheType() != mIn, "Cache options are not compatible with target cache type (cacheType != in).");
      notNullArg(opts.imp(), "You must provide valid options with proper cache implementation (imp).");

      mOpts = opts;

      return this;
    }

    @Override
    public CacheOptions opts() {
      return mOpts;
    }

    @Override
    public Class type() {
      return mValueType;
    }

    @Override
    public final CarboniteBuilder builder() {
      return mBuilder;
    }

    // implements CarboniteBuilder
    @Override
    public final CacheBuilder and(CacheType type) {
      return retaining(type()).in(type);
    }

    @Override
    public final Context context() {
      return builder().context();
    }

    @Override
    public final CacheBuilder retaining(Class type) {
      return builder().retaining(type);
    }

    @Override
    public final CarboniteBuilder iLoveYou() {
      return builder().iLoveYou();
    }

    @Override
    public final Carbonite iKnow() {
      return builder().iKnow();
    }

    @Override
    public final Carbonite build() {
      return builder().build();
    }

    //
  }
}

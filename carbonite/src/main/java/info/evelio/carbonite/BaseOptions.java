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
import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.CacheFactory;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.Carbonite.Defaults.CAPACITY;
import static info.evelio.carbonite.Carbonite.Defaults.FACTORY;
import static info.evelio.carbonite.Carbonite.Defaults.LOAD_FACTOR;
import static info.evelio.carbonite.Carbonite.Defaults.TYPE;
import static info.evelio.carbonite.util.Util.illegalArg;
import static info.evelio.carbonite.util.Util.notNullArg;


public class BaseOptions implements CarboniteBuilder.Options {
  private final CarboniteBuilder mCarboniteBuilder;
  private final Class<? extends Object> mRetaining;

  private int mCapacity = CAPACITY;
  private float mLoadFactor = LOAD_FACTOR;
  private CacheType mIn = TYPE;
  private CacheFactory mFactory = FACTORY;

  private Class<? extends Cache> mImp;

  public BaseOptions(CarboniteBuilder carboniteBuilder, Class<? extends Object> retaining) {
    notNullArg(carboniteBuilder, "Builder must not be null.");
    notNullArg(retaining, "Retaining class must not be null.");

    mCarboniteBuilder = carboniteBuilder;
    mRetaining = retaining;
  }

  @Override
  public CarboniteBuilder.Options in(CacheType type) {
    notNullArg(type, "CacheType in must not be null.");

    mIn = type;

    return this;
  }

  @Override
  public CarboniteBuilder.Options capacity(int capacity) {
    illegalArg(capacity < 0, "Negative capacity is not allowed.");

    mCapacity = capacity;

    return this;
  }

  @Override
  public CarboniteBuilder.Options loadFactor(float loadFactor) {
    illegalArg(loadFactor <= 0.0f || Float.isNaN(loadFactor), "Invalid load factor.");

    mLoadFactor = loadFactor;

    return this;
  }

  @Override
  public CarboniteBuilder.Options imp(Class<? extends Cache> imp) {
    mImp = imp;
    return this;
  }

  @Override
  public CarboniteBuilder.Options factory(CacheFactory factory) {
    notNullArg(factory, "You must provide a valid factory.");

    mFactory = factory;

    return this;
  }

  @Override
  public CacheType in() {
    return mIn;
  }

  @Override
  public int capacity() {
    return mCapacity;
  }

  @Override
  public float loadFactor() {
    return mLoadFactor;
  }

  @Override
  public Class<? extends Cache> imp() {
    return mImp;
  }

  @Override
  public CacheFactory factory() {
    return mFactory;
  }

  @Override
  public CarboniteBuilder builder() {
    return mCarboniteBuilder;
  }

  @Override
  public Class<? extends Object> retaining() {
    return mRetaining;
  }

  @Override
  public Options and(CacheType type) {
    return retaining(mRetaining).in(type);
  }

  // Wrapped methods for CarboniteBuilder

  @Override
  public Context context() {
    return mCarboniteBuilder.context();
  }

  @Override
  public Options retaining(Class type) {
    return mCarboniteBuilder.retaining(type);
  }

  @Override
  public CarboniteBuilder iLoveYou() {
    return mCarboniteBuilder.iLoveYou();
  }

  @Override
  public Carbonite iKnow() {
    return mCarboniteBuilder.iKnow();
  }

  @Override
  public Carbonite build() {
    return mCarboniteBuilder.build();
  }
  //
}
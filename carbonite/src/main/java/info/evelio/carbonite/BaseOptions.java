package info.evelio.carbonite;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.Carbonite.Defaults.*;
import static info.evelio.carbonite.Util.illegalArg;
import static info.evelio.carbonite.Util.notNullArg;

public class BaseOptions implements CarboniteBuilder.Options {
  private final CarboniteBuilder mCarboniteBuilder;
  private int mCapacity = CAPACITY;
  private float mLoadFactor = LOAD_FACTOR;
  private boolean mNullValues = NULL_VALUES;
  private CacheType mOn = TYPE;
  private CacheFactory mFactory = FACTORY;

  private Class<? extends Cache> mImp;

  public BaseOptions(CarboniteBuilder carboniteBuilder) {
    notNullArg(carboniteBuilder, "Builder must not be null.");

    mCarboniteBuilder = carboniteBuilder;
  }

  @Override
  public CarboniteBuilder.Options on(CacheType type) {
    notNullArg(type, "CacheType on must not be null.");

    mOn = type;

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
  public CarboniteBuilder.Options nullValues(boolean allow) {
    mNullValues = allow;
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
  public CacheType on() {
    return mOn;
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
  public boolean nullValues() {
    return mNullValues;
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

}
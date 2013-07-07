package info.evelio.carbonite;

public class BaseOptions implements CarboniteBuilder.Options {
  private final CarboniteBuilder mCarboniteBuilder;
  /*package*/ int mMemory = Carbonite.Defaults.MEMORY_VALUE;
  /*package*/ int mStorage = Carbonite.Defaults.STORAGE_VALUE;
  /*package*/ boolean mNullValues = Carbonite.Defaults.NULL_VALUES;

  public BaseOptions(CarboniteBuilder carboniteBuilder) {
    Util.notNullArg(carboniteBuilder, "Builder must not be null.");

    mCarboniteBuilder = carboniteBuilder;
  }

  @Override
  public CarboniteBuilder.Options inMemory(int capacity) {
    mMemory = capacity;
    return null;
  }

  @Override
  public CarboniteBuilder.Options inStorage(int capacity) {
    mStorage = capacity;
    return null;
  }

  @Override
  public CarboniteBuilder.Options nullValues(boolean allow) {
    mNullValues = allow;
    return null;
  }

  @Override
  public CarboniteBuilder plus() {
    return mCarboniteBuilder;
  }

  @Override
  public CarboniteBuilder and() {
    return mCarboniteBuilder;
  }

  @Override
  public int inMemory() {
    return mMemory;
  }

  @Override
  public int inStorage() {
    return mStorage;
  }

  @Override
  public boolean nullValues() {
    return mNullValues;
  }
}
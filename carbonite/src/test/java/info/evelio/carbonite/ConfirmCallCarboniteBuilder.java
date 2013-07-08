package info.evelio.carbonite;

import android.content.Context;

import static org.fest.assertions.api.Assertions.assertThat;

/*package*/ class ConfirmCallCarboniteBuilder implements CarboniteBuilder {
  private boolean mCalled;

  public ConfirmCallCarboniteBuilder() {
    reset();
  }

  public void reset() {
    mCalled = false;
  }

  private void called() {
    assertReseted();
    mCalled = true;
  }

  public void assertItWasCalled() {
    assertThat(mCalled)
        .overridingErrorMessage("Method was not called.")
        .isTrue();
  }

  private void assertReseted() {
    assertThat(mCalled)
        .overridingErrorMessage("You must call reset if you are reusing this ConfirmCallCarboniteBuilder instance")
        .isFalse();
  }

  @Override
  public Context context() {
    called();
    return null;
  }

  @Override
  public Options retaining(Class type) {
    called();
    return null;
  }

  @Override
  public CarboniteBuilder iLoveYou() {
    called();
    return null;
  }

  @Override
  public Carbonite iKnow() {
    called();
    return null;
  }

  @Override
  public Carbonite build() {
    called();
    return null;
  }
}
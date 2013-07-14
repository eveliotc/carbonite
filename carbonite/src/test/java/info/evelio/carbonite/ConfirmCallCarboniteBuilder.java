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
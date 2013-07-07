package info.evelio.carbonite.future;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class PresentTest {
  @Test public void presentWithNullGetsNull() throws ExecutionException, InterruptedException {
    final Present present = new Present(null);
    Assert.assertEquals(null, present.get());
  }

  @Test public void presentWithObjGetsObj() throws ExecutionException, InterruptedException {
    final Object obj = new Object();
    Present present = new Present(obj);
    Assert.assertEquals(obj, present.get());
  }
}

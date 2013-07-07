package info.evelio.carbonite;

import java.util.concurrent.Future;

public interface CarboniteApi {
  public <T> Carbonite set(String key, T value);
  public <T> Future<T> get(String key, Class<T> type);

  public <T> Carbonite memory(String key, T value);
  public <T> Carbonite storage(String key, T value);

  public <T> T memory(String key, Class<T> type);
  public <T> T storage(String key, Class<T> type);
}

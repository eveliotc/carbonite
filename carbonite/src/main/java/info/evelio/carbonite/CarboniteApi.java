package info.evelio.carbonite;

import java.util.concurrent.Future;

public interface CarboniteApi {
  <T> Carbonite set(String key, T value);
  <T> Future<T> get(String key, Class<T> type);

  <T> Carbonite memory(String key, T value);
  <T> Carbonite storage(String key, T value);

  <T> T memory(String key, Class<T> type);
  <T> T storage(String key, Class<T> type);
}

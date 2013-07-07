package info.evelio.carbonite;

public interface CarboniteBuilder {
  public Options retaining(Class type);
  public CarboniteBuilder iLoveYou();
  public Carbonite iKnow();

  public interface Options {
    public Options inMemory(int capacity);
    public Options inStorage(int capacity);
    public Options nullValues(boolean allow);
    public CarboniteBuilder plus();
    public CarboniteBuilder and();

    public int inMemory();
    public int inStorage();
    public boolean nullValues();
  }
}

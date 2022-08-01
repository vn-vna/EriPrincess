package vn.vna.eri.v2.utils.helper;

import vn.vna.eri.v2.utils.UTKeyValuePair;

public class PlaceholderEntry
    extends UTKeyValuePair<String, String> {

  public PlaceholderEntry(String key, String value) {
    this.setKey(key);
    this.setValue(value);
  }

  public static PlaceholderEntry entry(String key, String value) {
    return new PlaceholderEntry(key, value);
  }

  public static PlaceholderEntry entry(String key, int value) {
    return new PlaceholderEntry(key, "" + value);
  }
}

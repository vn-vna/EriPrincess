package vn.vna.eri.v2.utils.helper;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Placeholder {
  @Getter
  @Setter
  private Map<String, String> placeholder;

  private Placeholder() {
    this.placeholder = new HashMap<>();
  }

  public Placeholder place(String key, String value) {
    this.placeholder.put(key, value);
    return this;
  }

  public static Placeholder createPlaceholder() {
    return new Placeholder();
  }
}

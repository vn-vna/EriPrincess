package vn.vna.eri.v2.utils;

import java.io.Serializable;

public interface UTJsonClass extends Serializable {

  default String toJson() {
    return UTJsonParser.getInstance().getParser().toJson(this);
  }

  static <T extends UTJsonClass> T fromJson(String json, Class<T> clazz) {
    return UTJsonParser.getInstance().getParser().fromJson(json, clazz);
  }
}

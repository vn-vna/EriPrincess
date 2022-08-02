package vn.vna.eri.v2.utils;

import java.io.Serializable;

public interface UTJsonClass
  extends Serializable {

  default String toJson() {
    return UTJsonParser.getInstance().getParser().toJson(this);
  }

}

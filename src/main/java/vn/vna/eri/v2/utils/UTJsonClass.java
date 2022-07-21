package vn.vna.eri.v2.utils;

import com.google.gson.Gson;
import java.io.Serializable;

public interface UTJsonClass extends Serializable {

  Gson gson = new Gson();


  default String toJson() {
    return UTJsonClass.gson.toJson(this);
  }
}

package vn.vna.eri.v2.utils;

import com.google.gson.Gson;

public interface UTJsonClass {

  Gson gson = new Gson();


  default String toJson() {
    return UTJsonClass.gson.toJson(this);
  }
}

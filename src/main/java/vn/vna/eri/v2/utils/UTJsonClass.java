package vn.vna.eri.v2.utils;

import com.google.gson.Gson;

public class UTJsonClass {

  private static final Gson gson;

  static {
    gson = new Gson();
  }

  public String toJson() {
    return gson.toJson(this);
  }
}

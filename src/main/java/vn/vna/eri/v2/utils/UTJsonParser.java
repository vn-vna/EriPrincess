package vn.vna.eri.v2.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import vn.vna.eri.v2.utils.helper.InstantJsonParser;

public class UTJsonParser {

  private static UTJsonParser instance;

  @Getter
  private Gson parser;

  private UTJsonParser() {
    GsonBuilder builder = new GsonBuilder();

    builder.registerTypeAdapter(Instant.class, new InstantJsonParser());

    parser = builder.create();
  }

  public static UTJsonParser getInstance() {
    synchronized (UTJsonParser.class) {
      if (Objects.isNull(UTJsonParser.instance)) {
        UTJsonParser.instance = new UTJsonParser();
      }
    }
    return UTJsonParser.instance;
  }

  public static <T extends UTJsonClass> String toJson(T object) {
    return UTJsonParser.getInstance().getParser().toJson(object);
  }

}

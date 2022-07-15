package vn.vna.eri.v2.app;

import vn.vna.eri.v2.services.ApiService;
import vn.vna.eri.v2.services.DiscordService;

public class Main {

  public static void main(String[] args) {
    ApiService.initialize(args);
    DiscordService.initialize();
  }

}

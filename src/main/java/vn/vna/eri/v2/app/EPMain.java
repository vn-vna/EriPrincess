package vn.vna.eri.v2.app;

import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.services.SVDiscord;

public class EPMain {

  public static void main(String[] args) {
    SVApiControl.initialize(args);
    SVDiscord.initialize();
  }

}

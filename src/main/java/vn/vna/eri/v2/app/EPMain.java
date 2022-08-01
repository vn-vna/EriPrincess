package vn.vna.eri.v2.app;

import vn.vna.eri.v2.configs.CFGlobalConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.services.SVDiscord;

public class EPMain {

  public static void main(String[] args) {
    CFGlobalConfig.getInstance().invokeUpdateAtStage(ConfigTargetLoadStage.PRE_START);

    CFLangPack.getInstance().loadLangPacks();

    SVApiControl.initialize(args);
    SVDiscord.initialize();
  }

}

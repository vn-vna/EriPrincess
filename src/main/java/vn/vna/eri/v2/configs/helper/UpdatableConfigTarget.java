package vn.vna.eri.v2.configs.helper;

import vn.vna.eri.v2.configs.CFGlobalConfig;

public interface UpdatableConfigTarget {

  default void update() {
    CFGlobalConfig.getInstance().loadConfigForObject(this);
  }

}

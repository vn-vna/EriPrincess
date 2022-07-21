package vn.vna.eri.v2.configs.helper;

import lombok.Getter;

@Getter
public enum ConfigTargetLoadStage {
  PRE_START("App pre-start"),
  SPING_SERVICE_READY("Spring boot application is ready"),
  DISCORD_SERVICE_READY("Discord bot service is ready");

  String stageName;

  ConfigTargetLoadStage(String stageName) {
    this.stageName = stageName;
  }
}

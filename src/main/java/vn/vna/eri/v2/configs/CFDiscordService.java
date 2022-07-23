package vn.vna.eri.v2.configs;

import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_PREFIX;
import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_TOKEN;
import static vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage.SPRING_SERVICE_READY;

import java.util.Objects;
import lombok.Getter;
import vn.vna.eri.v2.configs.helper.ConfigTarget;
import vn.vna.eri.v2.configs.helper.LoadConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;

@Getter
@ConfigTarget(name = "dsc-service", stage = SPRING_SERVICE_READY)
public class CFDiscordService implements UpdatableConfigTarget {

  private static CFDiscordService instance;

  @LoadConfig(CFG_BOT_TOKEN)
  private String botToken;
  @LoadConfig(CFG_BOT_PREFIX)
  private String botPrefix;

  public static CFDiscordService getInstance() {
    synchronized (CFDiscordService.class) {
      if (Objects.isNull(CFDiscordService.instance)) {
        CFDiscordService.instance = new CFDiscordService();
      }
    }
    return CFDiscordService.instance;
  }
}

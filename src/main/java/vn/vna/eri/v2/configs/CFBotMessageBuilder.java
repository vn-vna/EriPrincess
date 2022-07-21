package vn.vna.eri.v2.configs;

import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_EMBED_FOOTER;
import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_EMBED_THUMB_URL;
import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_EMBED_TITLE;
import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_EMBED_TITLE_URL;
import static vn.vna.eri.v2.configs.CFGlobalConfig.CFG_BOT_NAME;
import static vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage.DISCORD_SERVICE_READY;

import java.util.Objects;
import lombok.Getter;
import vn.vna.eri.v2.configs.helper.ConfigTarget;
import vn.vna.eri.v2.configs.helper.LoadConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;

@Getter
@ConfigTarget(DISCORD_SERVICE_READY)
public class CFBotMessageBuilder implements UpdatableConfigTarget {

  @LoadConfig(CFG_BOT_NAME)
  private String botName;
  @LoadConfig(CFG_BOT_EMBED_TITLE)
  private String botEmbedTitle;
  @LoadConfig(CFG_BOT_EMBED_TITLE_URL)
  private String botEmbedTitleUrl;
  @LoadConfig(CFG_BOT_EMBED_THUMB_URL)
  private String botEmbedThumbUrl;
  @LoadConfig(CFG_BOT_EMBED_FOOTER)
  private String botEmbedFooter;

  private static CFBotMessageBuilder instance;

  public static CFBotMessageBuilder getInstance() {
    synchronized (CFBotMessageBuilder.class) {
      if (Objects.isNull(CFBotMessageBuilder.instance)) {
        CFBotMessageBuilder.instance = new CFBotMessageBuilder();
      }
    }
    return CFBotMessageBuilder.instance;
  }

}

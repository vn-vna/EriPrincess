package vn.vna.eri.v2.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import vn.vna.eri.v2.configs.CFGlobalConfig;
import vn.vna.eri.v2.configs.annotation.LoadConfig;

public final class UTMessageBuilder {

  private static UTMessageBuilder instance;
  private final CFGlobalConfig globalConfig;

  @LoadConfig(CFGlobalConfig.CFG_BOT_NAME)
  private String botName;
  @LoadConfig(CFGlobalConfig.CFG_BOT_EMBED_TITLE)
  private String botEmbedTitle;
  @LoadConfig(CFGlobalConfig.CFG_BOT_EMBED_TITLE_URL)
  private String botEmbedTitleUrl;
  @LoadConfig(CFGlobalConfig.CFG_BOT_EMBED_THUMB_URL)
  private String botEmbedThumbUrl;
  @LoadConfig(CFGlobalConfig.CFG_BOT_EMBED_FOOTER)
  private String botEmbedFooter;

  public UTMessageBuilder() {
    this.globalConfig = CFGlobalConfig.getInstance();
    this.updateConfig();
  }

  public static UTMessageBuilder getInstance() {
    return UTMessageBuilder.instance;
  }

  public static void initializeUtility() {
    UTMessageBuilder.instance = new UTMessageBuilder();
  }

  public void updateConfig() {
    this.globalConfig.loadConfigForObject(this);
  }

  public EmbedBuilder getBotDefaultEmbedBuilder() {
    return new EmbedBuilder()
        .setTitle(this.botEmbedTitle, this.botEmbedTitleUrl)
        .setFooter(this.botEmbedFooter)
        .setThumbnail(this.botEmbedThumbUrl);
  }

}

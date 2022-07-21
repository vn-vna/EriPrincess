package vn.vna.eri.v2.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import vn.vna.eri.v2.configs.CFBotMessageBuilder;

public final class UTMessageBuilder {

  private static UTMessageBuilder instance;

  private final CFBotMessageBuilder msgBuilderCfg;

  public UTMessageBuilder() {
    this.msgBuilderCfg = CFBotMessageBuilder.getInstance();
  }

  public static UTMessageBuilder getInstance() {
    return UTMessageBuilder.instance;
  }

  public static void initializeUtility() {
    UTMessageBuilder.instance = new UTMessageBuilder();
  }

  public EmbedBuilder getBotDefaultEmbedBuilder() {
    return new EmbedBuilder()
        .setTitle(
            msgBuilderCfg.getBotEmbedTitle(),
            msgBuilderCfg.getBotEmbedTitleUrl())
        .setFooter(msgBuilderCfg.getBotEmbedFooter())
        .setThumbnail(msgBuilderCfg.getBotEmbedThumbUrl());

  }

}

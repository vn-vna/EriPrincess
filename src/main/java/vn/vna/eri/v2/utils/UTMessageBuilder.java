package vn.vna.eri.v2.utils;

import java.util.Objects;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import vn.vna.eri.v2.configs.CFBotMessageBuilder;

public final class UTMessageBuilder {

  private static UTMessageBuilder instance;

  private final CFBotMessageBuilder msgBuilderCfg;

  public UTMessageBuilder() {
    this.msgBuilderCfg = CFBotMessageBuilder.getInstance();
  }

  public static UTMessageBuilder getInstance() {
    synchronized (UTMessageBuilder.class) {
      if (Objects.isNull(UTMessageBuilder.instance)) {
        UTMessageBuilder.instance = new UTMessageBuilder();
      }
    }
    return UTMessageBuilder.instance;
  }

  public EmbedBuilder getBotDefaultEmbedBuilder() {
    EmbedBuilder builder = new EmbedBuilder();
    Optional.ofNullable(this.msgBuilderCfg.getBotEmbedTitle()).ifPresent(builder::setTitle);
    Optional.ofNullable(this.msgBuilderCfg.getBotEmbedThumbUrl()).ifPresent(builder::setThumbnail);
    Optional.ofNullable(this.msgBuilderCfg.getBotEmbedFooter()).ifPresent(builder::setFooter);
    return builder;
  }

}

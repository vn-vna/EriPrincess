package vn.vna.eri.v2.utils;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.text.StrSubstitutor;
import org.ini4j.Ini;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFBotMessageBuilder;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.error.ERDiscordGuildPermissionMismatch;
import vn.vna.eri.v2.schema.DCGuildConfig;
import vn.vna.eri.v2.utils.helper.Placeholder;

public final class UTMessageBuilder {

  public static final String ZEROWIDTH_WHITE_SPACE   = "\u200b";
  public static final String LANG_PACK_SECTION       = "msg-builder";
  public static final String LPK_PERMMISSING_TITLE   = "tpl.perm-missing.title";
  public static final String LPK_PERMMISSING_PERMSTR = "tpl.perm-missing.perm-str";

  private static UTMessageBuilder instance;

  private final CFBotMessageBuilder  msgBuilderCfg;
  private final CLDiscordGuildConfig guildConfigClient;

  public UTMessageBuilder() {
    this.msgBuilderCfg     = CFBotMessageBuilder.getInstance();
    this.guildConfigClient = CLDiscordGuildConfig.getClient();
  }

  public static UTMessageBuilder getInstance() {
    synchronized (UTMessageBuilder.class) {
      if (Objects.isNull(UTMessageBuilder.instance)) {
        UTMessageBuilder.instance = new UTMessageBuilder();
      }
    }
    return UTMessageBuilder.instance;
  }

  public EmbedBuilder getDefaultEmbedBuilder() {
    EmbedBuilder builder = new EmbedBuilder();
    Optional.ofNullable(this.msgBuilderCfg.getBotEmbedTitle())
        .ifPresent(builder::setTitle);
    Optional.ofNullable(this.msgBuilderCfg.getBotEmbedThumbUrl())
        .ifPresent(builder::setThumbnail);
    Optional.ofNullable(this.msgBuilderCfg.getBotEmbedFooter())
        .ifPresent(builder::setFooter);
    return builder;
  }

  public Message getPermissionMissingMessage(ERDiscordGuildPermissionMismatch pmex) {
    String                  guildId     = pmex.getMember().getGuild().getId();
    EmbedBuilder            errEmbed    = this.getDefaultEmbedBuilder();
    StringBuilder           permErrStr  = new StringBuilder();
    List<Permission>        mismatch    = pmex.getMismatchPermission();
    Optional<DCGuildConfig> guildConfig = this.guildConfigClient.getConfiguration(guildId);

    Optional<Ini> langPack = CFLangPack
        .getInstance()
        .getLangPack(guildConfig
            .map((cfg) -> cfg.getLanguage()).orElse(CFLangPack.DEFAULT_LANG_PACK.getName()));

    String templateTitle = langPack
        .map((pack) -> pack.get(LANG_PACK_SECTION, LPK_PERMMISSING_TITLE))
        .orElse("");
    String templateElem  = langPack
        .map((pack) -> pack.get(LANG_PACK_SECTION, LPK_PERMMISSING_PERMSTR))
        .orElse("");

    for (Permission perm : mismatch) {
      Placeholder pl = this.createPlaceholder()
          .place("emoji", ":no_entry_sign:")
          .place("perm_name", perm.getName())
          .place("endl", "\n");
      permErrStr.append(this.formatMessage(templateElem, pl));
    }

    Placeholder pl = this.createPlaceholder()
        .place("count", Objects.toString(mismatch.size()))
        .place("plural", mismatch.size() > 1 ? "s" : "")
        .place("user", pmex.getMember().getEffectiveName());

    errEmbed.addField(
        this.formatMessage(templateTitle, pl),
        permErrStr.toString(),
        false);

    errEmbed.setColor(Color.RED);
    errEmbed.setThumbnail("https://cdn-icons-png.flaticon.com/512/5219/5219070.png");

    MessageBuilder msg = new MessageBuilder();
    msg.setEmbeds(errEmbed.build());

    return msg.build();
  }

  public String formatMessage(String format, Placeholder placeholders) {
    return StrSubstitutor.replace(format, placeholders.getPlaceholder());
  }

  public Placeholder createPlaceholder() {
    return Placeholder.createPlaceholder();
  }

}

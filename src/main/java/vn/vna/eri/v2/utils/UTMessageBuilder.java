package vn.vna.eri.v2.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.text.StrSubstitutor;
import vn.vna.eri.v2.configs.CFBotMessageBuilder;
import vn.vna.eri.v2.error.ERDiscordGuildPermissionMismatch;

public final class UTMessageBuilder {

  public static final String ZEROWIDTH_WHITE_SPACE  = "\u200b";
  public static final String TPL_PERM_MISSING_STR   = "${emoji} -- [${perm_name}]\n";
  public static final String TPL_PERM_MISSING_TITLE = "Missing ${count} permission${plural} for user [${user}]";

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

  public Message getPermissionMissingMessage(ERDiscordGuildPermissionMismatch pmex) {
    EmbedBuilder     errEmbed   = UTMessageBuilder.getInstance().getBotDefaultEmbedBuilder();
    StringBuilder    permErrStr = new StringBuilder();
    List<Permission> mismatch   = pmex.getMismatchPermission();

    for (Permission perm : mismatch) {
      Map<String, String> fmtPermData = new HashMap<>();
      fmtPermData.put("emoji", ":no_entry_sign:");
      fmtPermData.put("perm_name", perm.getName());
      permErrStr.append(StrSubstitutor.replace(TPL_PERM_MISSING_STR, fmtPermData));
    }

    Map<String, String> fmtTitleData = new HashMap<>();
    fmtTitleData.put("count", "" + mismatch.size());
    fmtTitleData.put("plural", mismatch.size() > 1 ? "s" : "");
    fmtTitleData.put("user", pmex.getMember().getEffectiveName());

    errEmbed.addField(
        StrSubstitutor.replace(TPL_PERM_MISSING_TITLE, fmtTitleData),
        permErrStr.toString(),
        false);

    errEmbed.setColor(Color.RED);
    errEmbed.setThumbnail("https://cdn-icons-png.flaticon.com/512/5219/5219070.png");

    MessageBuilder msg = new MessageBuilder();
    msg.setEmbeds(errEmbed.build());

    return msg.build();
  }

}

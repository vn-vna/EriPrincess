package vn.vna.eri.v2.event.discord;

import static net.dv8tion.jda.api.Permission.MANAGE_SERVER;
import static vn.vna.eri.v2.configs.CFLangPack.SECTION_CMD;
import static vn.vna.eri.v2.event.discord.helper.CommandType.SUBCOMMAND;
import static vn.vna.eri.v2.utils.helper.PlaceholderEntry.entry;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.utils.UTMessageBuilder;

@CommandProperties(
  commands = "props",
  parent = CMDAdmin.class,
  type = SUBCOMMAND,
  descriptionKey = "cmd.desc.cmd-admin-props",
  senderPermission = { MANAGE_SERVER })
public class CMDAdminProps
  extends CMDTemplate {

  public static final String LPK_TITLE               = "cmd.tpl.cmd-admin-props.msg-title";
  public static final String LPK_KEYPAIR             = "cmd.tpl.cmd-admin-props.key-pair";
  public static final String LPK_PROPNAME_GUILDID    = "cmd.txt.cmd-admin-props.guild-id";
  public static final String LPK_PROPNAME_REGISTERED = "cmd.txt.cmd-admin-props.registered";
  public static final String LPK_PROPNAME_LANGUAGE   = "cmd.txt.cmd-admin-props.language";
  public static final String LPK_PROPNAME_TIMEZONE   = "cmd.txt.cmd-admin-props.timezone";

  CFLangPack       langPackMng = CFLangPack.getInstance();
  UTMessageBuilder msgBuilder  = UTMessageBuilder.getInstance();

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent msgEvent) {
      Guild          guild   = msgEvent.getGuild();
      MessageChannel channel = msgEvent.getChannel();
      String         guildId = guild.getId();
      EmbedBuilder   embed   = msgBuilder.getDefaultEmbedBuilder();
      MessageBuilder msg     = new MessageBuilder();

      CLDiscordGuildConfig configClient = CLDiscordGuildConfig.getClient();

      configClient.getConfiguration(guildId)
        .ifPresentOrElse((result) -> {
          StringBuilder sb      = new StringBuilder();
          String      guildLang = result.getLanguage();
          String      tplTitle  = langPackMng.getString(guildLang, SECTION_CMD, LPK_TITLE)
            .orElse("");

          this.appendProperty(sb, guildLang,
            LPK_PROPNAME_GUILDID, guildId);

          this.appendProperty(sb, guildLang,
            LPK_PROPNAME_REGISTERED, result.getJoinedDateTime().toString());

          this.appendProperty(sb, guildLang,
            LPK_PROPNAME_LANGUAGE, result.getLanguage());

          this.appendProperty(sb, guildLang,
            LPK_PROPNAME_TIMEZONE, this.getTimezoneString(result.getTimeZone()));

          embed.addField(tplTitle, sb.toString(), false);

        }, () -> {

        });

      msg.setEmbeds(embed.build());
      channel.sendMessage(msg.build()).queue();
    }
  }

  private void appendProperty(StringBuilder sb, String lang, String tplKey, String value) {
    String tplKeyPair = langPackMng.getString(lang, SECTION_CMD, LPK_KEYPAIR)
      .orElse("");

    // Append property
    String tzPropName = this.langPackMng.getString(lang, SECTION_CMD, tplKey)
      .orElse("");
    sb.append(msgBuilder.formatMessage(tplKeyPair,
      entry("props", tzPropName),
      entry("value", value)));
  }

  private String getTimezoneString(Integer tz) {
    StringBuilder sb = new StringBuilder();
    sb.append("GMT")
      .append(tz > 0 ? "+" : "")
      .append(tz);

    return sb.toString();
  }

}

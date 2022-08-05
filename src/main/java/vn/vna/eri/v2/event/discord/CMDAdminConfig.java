package vn.vna.eri.v2.event.discord;

import static net.dv8tion.jda.api.Permission.MANAGE_CHANNEL;
import static net.dv8tion.jda.api.Permission.MANAGE_SERVER;
import static vn.vna.eri.v2.configs.CFLangPack.SECTION_CMD;
import static vn.vna.eri.v2.utils.helper.MessageMentionType.MENTION_CHANNEL_TEXT;
import static vn.vna.eri.v2.utils.helper.PlaceholderEntry.entry;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.event.discord.helper.CommandType;
import vn.vna.eri.v2.schema.DCGuildConfig;
import vn.vna.eri.v2.utils.UTMessageBuilder;
import vn.vna.eri.v2.utils.UTStringParser;

@CommandProperties(
  commands = { "config", "cfg" },
  senderPermission = {
    MANAGE_SERVER,
    MANAGE_CHANNEL },
  type = CommandType.SUBCOMMAND,
  parent = CMDAdmin.class,
  descriptionKey = "cmd.desc.cmd-admin-config"
)
public class CMDAdminConfig
  extends CMDTemplate {

  public static final String CMD_CFG_AIRPORT  = "airport";
  public static final String CMD_CFG_LANGUAGE = "lang";
  public static final String LPK_CFG_NCFG     = "cmd.tpl.cmd-admin-config.no-config";
  public static final String LPK_CFG_ERROR    = "cmd.tpl.cmd-admin-config.error";
  public static final String LPK_CFG_SUCCESS  = "cmd.tpl.cmd-admin-config.success";

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    // [admin,config,airport,<#3u259934928875>]
    if (event instanceof MessageReceivedEvent msgEvent) {
      CLDiscordGuildConfig cfgClient   = CLDiscordGuildConfig.getClient();
      CFLangPack           langPackMng = CFLangPack.getInstance();
      UTMessageBuilder     msgBuilder  = UTMessageBuilder.getInstance();

      Guild  guild   = msgEvent.getGuild();
      String guildId = guild.getId();

      MessageBuilder msg = new MessageBuilder();

      cfgClient
        .getConfiguration(guildId)
        .ifPresentOrElse((cfg) -> {
          String tplSuccessMsg = langPackMng
            .getString(cfg.getLanguage(), SECTION_CMD, LPK_CFG_SUCCESS)
            .orElse("");
          String tplErrMsg   = langPackMng
            .getString(cfg.getLanguage(), SECTION_CMD, LPK_CFG_ERROR)
            .orElse("");

          if (commandList.length < 4) {
            return;
          }

          String prop = commandList[commandDepth + 1];

          switch (prop) {
          case CMD_CFG_AIRPORT ->
            UTStringParser
              .parseMention(commandList[commandDepth + 2])
              .ifPresent((mention) -> {
                if (mention.type() != MENTION_CHANNEL_TEXT) {
                  msg.setContent(msgBuilder
                    .formatMessage(tplErrMsg,
                      entry("guild", guild.getName())));
                  return;
                }

                DCGuildConfig update = new DCGuildConfig();
                update.setGuildId(guildId);
                update.setAirportChannel(mention.targetId());
                cfgClient.updateConfig(guildId, update)
                  .ifPresent((result) -> {
                    TextChannel airport = guild.getTextChannelById(
                      result.getAirportChannel());

                    msg.setContent(msgBuilder
                      .formatMessage(tplSuccessMsg,
                        entry("field", CMD_CFG_AIRPORT),
                        entry("value", airport.getAsMention())));
                  });

              });
          case CMD_CFG_LANGUAGE -> {
            String langParam = commandList[commandDepth + 2];
            langPackMng
              .getLangPack(langParam)
              .ifPresent((pack) -> {
                DCGuildConfig update = new DCGuildConfig();
                update.setGuildId(guildId);
                update.setLanguage(langParam);

                cfgClient.updateConfig(guildId, update)
                  .ifPresentOrElse((result) -> {
                    msg.setContent(msgBuilder
                      .formatMessage(tplSuccessMsg,
                        entry("field", CMD_CFG_LANGUAGE),
                        entry("value", langParam)));
                  }, () -> {
                    msg.setContent(msgBuilder
                      .formatMessage(tplErrMsg,
                        entry("field", CMD_CFG_LANGUAGE),
                        entry("value", langParam)));
                  });
              });
          }
          }
        }, () -> {
          String tplNoConfig = langPackMng
            .getString(SECTION_CMD, LPK_CFG_NCFG)
            .orElse("");

          msg.setContent(msgBuilder
            .formatMessage(tplNoConfig,
              entry("guild", guild.getName())));
        });

      msgEvent.getChannel()
        .sendMessage(msg.build())
        .queue();
    }
  }

}

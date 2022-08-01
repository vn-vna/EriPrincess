package vn.vna.eri.v2.event.discord;

import static net.dv8tion.jda.api.Permission.MANAGE_CHANNEL;
import static net.dv8tion.jda.api.Permission.MANAGE_PERMISSIONS;
import static net.dv8tion.jda.api.Permission.MANAGE_ROLES;
import static net.dv8tion.jda.api.Permission.MANAGE_SERVER;
import static vn.vna.eri.v2.configs.CFLangPack.SECTION_CMD;
import static vn.vna.eri.v2.event.discord.helper.CommandType.SUBCOMMAND;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.error.ERDiscordGuildPermissionMismatch;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.services.SVDiscord;
import vn.vna.eri.v2.utils.UTMessageBuilder;

@CommandProperties(
    type = SUBCOMMAND,
    commands = "register",
    descriptionKey = "cmd.desc.cmd-admin-register",
    parent = CMDAdmin.class,
    senderPermission = {
        MANAGE_SERVER,
        MANAGE_CHANNEL,
        MANAGE_PERMISSIONS,
        MANAGE_ROLES })
public class CMDAdminRegister
    extends CMDTemplate {

  private static String TPL_GUILD_EXISTS_TITLE  = "cmd.tpl.cmd-admin-register.exists.title";
  private static String TPL_GUILD_EXISTS_VAL    = "cmd.tpl.cmd-admin-register.exists.val";
  private static String TPL_GUILD_CREATED_TITLE = "cmd.tpl.cmd-admin-register.created.title";
  private static String TPL_GUILD_CREATED_VAL   = "cmd.tpl.cmd-admin-register.created.val";

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent msgEvent) {

      CLDiscordGuildConfig configClient = CLDiscordGuildConfig.getClient();
      UTMessageBuilder     msgBuidler   = UTMessageBuilder.getInstance();

      String         guildId      = msgEvent.getGuild().getId();
      Member         bot          = SVDiscord.getInstance().getSelfAsMember(msgEvent.getGuild());
      Member         sender       = msgEvent.getMember();
      GuildChannel   guildChannel = msgEvent.getGuildChannel();
      EmbedBuilder   embed        = UTMessageBuilder.getInstance().getDefaultEmbedBuilder();
      MessageBuilder msg          = new MessageBuilder();
      CFLangPack     langPackMng  = CFLangPack.getInstance();

      try {
        this.requirePermissionMessageEvent(bot, sender, guildChannel);
        configClient
            .getConfiguration(guildId)
            .ifPresentOrElse((cfg) -> {
              String existTitleTpl = langPackMng
                  .getString(cfg.getLanguage(), SECTION_CMD, TPL_GUILD_EXISTS_TITLE)
                  .orElse("");
              String existValTpl = langPackMng
                  .getString(cfg.getLanguage(), SECTION_CMD, TPL_GUILD_EXISTS_VAL)
                  .orElse("");

              embed.addField(
                  msgBuidler.formatMessage(existTitleTpl),
                  msgBuidler.formatMessage(existValTpl),
                  false);
            }, () -> {
              configClient
                  .createConfig(guildId)
                  .ifPresent((cfg) -> {
                    String createdTitleTpl = langPackMng
                        .getString(cfg.getLanguage(), SECTION_CMD, TPL_GUILD_CREATED_TITLE)
                        .orElse("");
                    String createdValTpl = langPackMng
                        .getString(cfg.getLanguage(), SECTION_CMD, TPL_GUILD_CREATED_VAL)
                        .orElse("");

                    embed.addField(
                        msgBuidler.formatMessage(createdTitleTpl),
                        msgBuidler.formatMessage(createdValTpl),
                        false);
                  });
            });

        msg.setEmbeds(embed.build());
        msgEvent.getChannel()
            .sendMessage(msg.build())
            .queue();
      } catch (ERDiscordGuildPermissionMismatch pmex) {
        msgEvent.getChannel()
            .sendMessage(UTMessageBuilder.getInstance().getPermissionMissingMessage(pmex))
            .queue();
      }
    }
  }

}

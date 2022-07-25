package vn.vna.eri.v2.event.discord;

import static net.dv8tion.jda.api.Permission.MANAGE_CHANNEL;
import static net.dv8tion.jda.api.Permission.MANAGE_PERMISSIONS;
import static net.dv8tion.jda.api.Permission.MANAGE_ROLES;
import static net.dv8tion.jda.api.Permission.MANAGE_SERVER;
import static vn.vna.eri.v2.event.discord.helper.CommandType.SUBCOMMAND;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.error.ERDiscordGuildPermissionMismatch;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.services.SVDiscord;
import vn.vna.eri.v2.utils.UTMessageBuilder;

@CommandProperties(
    type = SUBCOMMAND,
    commands = "register",
    parent = CMDAdmin.class,
    senderPermission = {
        MANAGE_SERVER,
        MANAGE_CHANNEL,
        MANAGE_PERMISSIONS,
        MANAGE_ROLES })
public class CMDAdminRegister
    extends CMDTemplate {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent msgEvent) {

      Member       bot          = SVDiscord.getInstance().getSelfAsMember(msgEvent.getGuild());
      Member       sender       = msgEvent.getMember();
      GuildChannel guildChannel = msgEvent.getGuildChannel();

      try {
        this.requirePermissionMessageEvent(bot, sender, guildChannel);
      } catch (ERDiscordGuildPermissionMismatch pmex) {
        msgEvent.getChannel()
            .sendMessage(UTMessageBuilder.getInstance().getPermissionMissingMessage(pmex))
            .queue();
      }
    }
  }

}

package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.helper.CommandType.MESSAGE_COMMAND;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.error.ERDiscordGuildPermissionMismatch;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;

@CommandProperties(
    type = MESSAGE_COMMAND, commands = "admin", botPermission = { Permission.ADMINISTRATOR }
)
public class CMDAdmin
    extends CMDDiscordCommand {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent messageReceivedEvent) {
      try {
        Guild        guild   = messageReceivedEvent.getGuild();
        Member       sender  = messageReceivedEvent.getMember();
        Member       bot     = this.getDiscordService().getSelfAsMember(guild);
        GuildChannel channel = messageReceivedEvent.getGuildChannel();

        this.requirePermissionMessageEvent(bot, sender, channel);
      } catch (ERDiscordGuildPermissionMismatch pex) {
        this.getCommandLogger().error("Execution failed due to missing permission {} of member {}",
            pex.getMismatchPermission(), pex.getMember().getEffectiveName());
      }
    }
  }

}

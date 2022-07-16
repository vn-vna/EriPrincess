package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.CMDDiscordCommand.CommandType.MESSAGE_COMMAND;

import net.dv8tion.jda.api.events.Event;
import vn.vna.eri.v2.event.discord.CMDDiscordCommand.CommandProperties;

@CommandProperties(
    commands = "help",
    type = MESSAGE_COMMAND,
    description = "No Description"
)
public class CMDHelp extends CMDDiscordCommand {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
  }

}

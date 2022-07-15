package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.DiscordCommand.CommandType.MESSAGE_COMMAND;

import net.dv8tion.jda.api.events.Event;
import vn.vna.eri.v2.event.discord.DiscordCommand.CommandProperties;

@CommandProperties(
    commands = "help",
    type = MESSAGE_COMMAND,
    description = "No Description"
)
public class HelpCommand extends DiscordCommand {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (commandDepth < commandList.length - 1) {
      // Has parameter
    }
  }

}

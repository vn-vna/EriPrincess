package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.helper.CommandType.MESSAGE_COMMAND;

import net.dv8tion.jda.api.events.Event;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;

@CommandProperties(
    commands = "help",
    type = MESSAGE_COMMAND,
    description = "No Description")
public class CMDHelp
    extends CMDTemplate {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
  }

}

package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.DiscordCommand.CommandType.MESSAGE_COMMAND;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.event.discord.DiscordCommand.CommandProperties;

@CommandProperties(type = MESSAGE_COMMAND, commands = "ping")
public class PingCommand extends DiscordCommand {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent messageReceived) {
      messageReceived
          .getChannel()
          .sendMessage("Gateway latency: " + messageReceived.getJDA().getGatewayPing() + " ms")
          .queue();
    }
  }

}

package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.helper.CommandType.MESSAGE_COMMAND;
import static vn.vna.eri.v2.event.discord.helper.CommandType.SUBCOMMAND;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;

@CommandProperties(
    type = MESSAGE_COMMAND,
    commands = "ping"
)
public class CMDPing extends CMDDiscordCommand {

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent messageReceived) {
      messageReceived
          .getChannel()
          .sendMessage("Gateway latency: " + messageReceived.getJDA().getGatewayPing() + " ms")
          .queue();
    }
  }

  @CommandProperties(
      commands = "hello",
      type = SUBCOMMAND,
      parent = CMDPing.class
  )
  public static class CMDPingHello extends CMDDiscordCommand {

    @Override
    public void execute(String[] commandList, Event event, Integer commandDepth) {
      if (event instanceof MessageReceivedEvent messageReceived) {
        messageReceived
            .getMessage()
            .getChannel()
            .sendMessage("Called nested ping")
            .queue();
      }
    }

  }


}

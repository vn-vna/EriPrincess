package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.DiscordCommand.CommandType.SUBCOMMAND;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.event.discord.DiscordCommand.CommandProperties;

@CommandProperties(commands = "hello", type = SUBCOMMAND, parent = PingCommand.class)
public class PingNested extends DiscordCommand {

  @Override
  public void execute(Event event) {
    if (event instanceof MessageReceivedEvent receivedEvent) {
      receivedEvent
          .getMessage()
          .reply("Called nested ping")
          .queue();
    }
  }

}

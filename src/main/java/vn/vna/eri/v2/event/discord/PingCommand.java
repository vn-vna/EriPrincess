package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.DiscordCommand.CommandType.MESSAGE_COMMAND;

import net.dv8tion.jda.api.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.event.discord.DiscordCommand.CommandProperties;

@CommandProperties(type = MESSAGE_COMMAND, commands = {"ping", "tping", "ok"})
public class PingCommand extends DiscordCommand {

  private static final Logger logger = LoggerFactory.getLogger(PingCommand.class);

  @Override
  public void execute(Event event) {
    logger.info("Ping");
  }

}

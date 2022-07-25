package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.event.discord.helper.CommandType.MESSAGE_COMMAND;

import java.util.Arrays;
import net.dv8tion.jda.api.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;

@CommandProperties(
    commands = "query",
    type = MESSAGE_COMMAND
)
public class CMDQuery
    extends CMDTemplate {

  public static final Logger logger = LoggerFactory.getLogger(CMDQuery.class);

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    logger.info(Arrays.toString(commandList));
  }

}

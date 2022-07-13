package vn.vna.eri.v2.configs;

import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.event.discord.MessageCommand;

import java.util.Set;

public class DiscordEventListener extends ListenerAdapter {

  private static final Logger logger;
  private static final String pkgSearch;

  static {
    logger = LoggerFactory.getLogger(DiscordEventListener.class);
    pkgSearch = MessageCommand.class.getPackageName();
  }

  public DiscordEventListener() {
    Reflections reflections = new Reflections(pkgSearch);
    Set<Class<? extends MessageCommand>> types = reflections.getSubTypesOf(MessageCommand.class);
    logger.info("Found {} command(s) from package {}", types.size(), pkgSearch);

    for (var type : types) {
      // Inject command that annotated from types
      type.getDeclaredAnnotation(MessageCommand.Command.class);
    }
  }

  @Override
  public void onMessageBulkDelete(MessageBulkDeleteEvent event) {
    super.onMessageBulkDelete(event);
  }

  @Override
  public void onMessageDelete(MessageDeleteEvent event) {
    super.onMessageDelete(event);
  }

  @Override
  public void onMessageEmbed(MessageEmbedEvent event) {
    super.onMessageEmbed(event);
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    super.onMessageReceived(event);
  }

  @Override
  public void onMessageUpdate(MessageUpdateEvent event) {
    super.onMessageUpdate(event);
  }

}

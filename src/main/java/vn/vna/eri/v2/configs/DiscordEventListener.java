package vn.vna.eri.v2.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordEventListener extends ListenerAdapter {

  static {
    logger = LoggerFactory.getLogger(DiscordEventListener.class);
  }

  public DiscordEventListener() {

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

  private static Logger logger;

  public static @interface MessageCommand {
  }
}

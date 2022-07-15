package vn.vna.eri.v2.configs;

import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.event.discord.DiscordCommand;

public class DiscordEventListener extends ListenerAdapter {

  private static final Logger logger;
  private static final String pkgSearch;

  static {
    logger = LoggerFactory.getLogger(DiscordEventListener.class);
    pkgSearch = DiscordCommand.class.getPackageName();
  }

  private final String botPrefix;

  public DiscordEventListener() {
    this.botPrefix = ConfigManager.getEnvManager().getString(Env.ENV_BOT_PREFIX);
    DiscordCommand.loadCommands();
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
    String rawContent = event.getMessage().getContentRaw();

    if (!rawContent.startsWith(this.botPrefix)) {
      return;
    }

    String[] commandArray = rawContent
        .substring(this.botPrefix.length())
        .split(" ");

    DiscordCommand.tryExecute(commandArray, event);
  }

  @Override
  public void onMessageUpdate(MessageUpdateEvent event) {
    super.onMessageUpdate(event);
  }

}

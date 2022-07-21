package vn.vna.eri.v2.configs;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage;
import vn.vna.eri.v2.event.discord.CMDDiscordCommand;
import vn.vna.eri.v2.event.discord.helper.CommandType;

public class CFDiscordEventListener extends ListenerAdapter {

  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(CFDiscordEventListener.class);
  }

  private final String botPrefix;

  public CFDiscordEventListener() {
    logger.info("Configuring discord event listener");
    this.botPrefix = CFGlobalConfig.getInstance().getString(CFGlobalConfig.ENV_BOT_PREFIX);
    logger.info("Bot prefix is being used: [{}]", this.botPrefix);
    CMDDiscordCommand.loadCommands();
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

    CMDDiscordCommand.tryExecute(commandArray, event, CommandType.MESSAGE_COMMAND);
  }

  @Override
  public void onMessageUpdate(MessageUpdateEvent event) {
    super.onMessageUpdate(event);
  }

  @Override
  public void onReady(ReadyEvent event) {
    super.onReady(event);
    CFGlobalConfig
        .getInstance()
        .invokeUpdateAtStage(ConfigTargetLoadStage.DISCORD_SERVICE_READY);
  }
}

package vn.vna.eri.v2.event.discord;

import java.util.Objects;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.CFDiscordService;
import vn.vna.eri.v2.event.discord.helper.CommandType;

@NoArgsConstructor
public class EMMessageEvent
    extends ListenerAdapter {

  private static final Logger   logger = LoggerFactory.getLogger(EMMessageEvent.class);
  private static EMMessageEvent instance;

  public static EMMessageEvent getInstance() {
    synchronized (EMMessageEvent.class) {
      if (Objects.isNull(EMMessageEvent.instance)) {
        EMMessageEvent.instance = new EMMessageEvent();
      }
    }
    return EMMessageEvent.instance;
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

    if (!rawContent.startsWith(CFDiscordService.getInstance().getBotPrefix())) {
      return;
    }

    String[] commandArray = rawContent
        .substring(CFDiscordService.getInstance().getBotPrefix().length()).split(" ");

    long    beginEx = System.currentTimeMillis();
    boolean success = CMDTemplate.tryExecute(commandArray, event, CommandType.MESSAGE_COMMAND);
    if (success) {
      logger.info(
          "Took {} ms: Executed a command required by {} on guild {} with message \"{}\"",
          System.currentTimeMillis() - beginEx,
          event.getMember().getEffectiveName(),
          event.getGuild().getName(),
          event.getMessage().getContentRaw());
    }
  }

  @Override
  public void onMessageUpdate(MessageUpdateEvent event) {
    super.onMessageUpdate(event);
  }

}

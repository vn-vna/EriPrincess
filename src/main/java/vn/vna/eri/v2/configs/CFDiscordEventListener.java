package vn.vna.eri.v2.configs;

import static vn.vna.eri.v2.configs.CFGlobalConfig.CT_NAME_DSC_EVLISTENER;
import static vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage.DISCORD_SERVICE_READY;

import java.util.Objects;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.helper.ConfigTarget;
import vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage;
import vn.vna.eri.v2.configs.helper.LoadConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;
import vn.vna.eri.v2.event.discord.CMDDiscordCommand;
import vn.vna.eri.v2.event.discord.helper.CommandType;

@NoArgsConstructor
@ConfigTarget(name = CT_NAME_DSC_EVLISTENER, stage = DISCORD_SERVICE_READY)
public class CFDiscordEventListener
    extends ListenerAdapter
    implements UpdatableConfigTarget {

  private static final Logger logger = LoggerFactory.getLogger(CFDiscordEventListener.class);
  private static CFDiscordEventListener instance;

  @LoadConfig(CFGlobalConfig.CFG_BOT_PREFIX)
  private String botPrefix;

  public static CFDiscordEventListener getInstance() {
    synchronized (CFDiscordEventListener.class) {
      if (Objects.isNull(CFDiscordEventListener.instance)) {
        CFDiscordEventListener.instance = new CFDiscordEventListener();
      }
    }
    return CFDiscordEventListener.instance;
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
  public void onSlashCommand(SlashCommandEvent event) {
    super.onSlashCommand(event);
  }

  @Override
  public void onReady(ReadyEvent event) {
    super.onReady(event);
    CFGlobalConfig
        .getInstance()
        .invokeUpdateAtStage(ConfigTargetLoadStage.DISCORD_SERVICE_READY);
    CMDDiscordCommand.loadCommands();
  }
}

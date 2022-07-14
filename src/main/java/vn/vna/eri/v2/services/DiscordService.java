package vn.vna.eri.v2.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.ConfigManager;
import vn.vna.eri.v2.configs.DiscordEventListener;
import vn.vna.eri.v2.configs.Env;
import vn.vna.eri.v2.error.DiscordServiceExists;
import vn.vna.eri.v2.schema.ServiceStatus;

public class DiscordService implements Runnable {

  private static final Logger logger;
  private static Thread serviceThread;
  private static DiscordService instance;

  static {
    logger = LoggerFactory.getLogger(DiscordService.class);
  }

  private final ServiceStatus serviceStatus;
  private JDA jdaContext;
  private DiscordEventListener eventListener;

  public DiscordService() {
    if (!Objects.isNull(DiscordService.instance)) {
      throw new DiscordServiceExists();
    }
    this.serviceStatus = new ServiceStatus();
    this.serviceStatus.setStatus(ServiceStatus.STATUS_OFFLINE);
    this.serviceStatus.setLastStartUp(null);

    DiscordService.instance = this;
  }

  public static void initialize() {
    if (ConfigManager.getEnvManager().getBoolean(Env.ENV_DISABLE_DISCORD)) {
      logger.warn("Discord bot service is disabled by default");
      return;
    }
    DiscordService.logger.info("Starting Discord service");
    DiscordService.serviceThread = new Thread(new DiscordService());
    serviceThread.start();
  }

  public static DiscordService getInstance() {
    return DiscordService.instance;
  }

  public JDA getJdaContext() {
    return this.jdaContext;
  }

  public ServiceStatus getStatus() {
    return this.serviceStatus;
  }

  public DiscordEventListener getEventListener() {
    return this.eventListener;
  }

  @Override
  public void run() {
    String token = ConfigManager.getEnvManager().getString(Env.ENV_BOT_TOKEN);

    List<GatewayIntent> intents = new ArrayList<>();
    intents.add(GatewayIntent.GUILD_MESSAGES);
    intents.add(GatewayIntent.GUILD_MEMBERS);
    intents.add(GatewayIntent.GUILD_BANS);
    intents.add(GatewayIntent.GUILD_EMOJIS);
    intents.add(GatewayIntent.GUILD_INVITES);
    intents.add(GatewayIntent.GUILD_MESSAGE_TYPING);
    intents.add(GatewayIntent.GUILD_PRESENCES);
    intents.add(GatewayIntent.GUILD_VOICE_STATES);
    intents.add(GatewayIntent.GUILD_WEBHOOKS);
    intents.add(GatewayIntent.GUILD_MESSAGE_REACTIONS);

    this.eventListener = new DiscordEventListener();

    JDABuilder jdaBuilder = JDABuilder.create(token, intents).addEventListeners(eventListener);

    try {
      this.jdaContext = jdaBuilder.build();

      // Update status
      this.serviceStatus.setStatus(ServiceStatus.STATUS_ONLINE);
      this.serviceStatus.setLastStartUp(Instant.now().toString());
    } catch (LoginException lex) {
      DiscordService.logger.error("Can't login to discord.");
      this.serviceStatus.setStatus(ServiceStatus.STATUS_ERROR);
    }
  }

}

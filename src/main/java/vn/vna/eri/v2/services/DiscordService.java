package vn.vna.eri.v2.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.vna.eri.v2.configs.ConfigManager;
import vn.vna.eri.v2.configs.DiscordEventListener;
import vn.vna.eri.v2.configs.Env;
import vn.vna.eri.v2.error.DiscordServiceExists;
import vn.vna.eri.v2.schema.ServiceStatus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordService implements Runnable {

  static {
    logger = LoggerFactory.getLogger(DiscordService.class);
  }

  public DiscordService() {
    if (!Objects.isNull(DiscordService.instance)) {
      throw new DiscordServiceExists();
    }

    DiscordService.instance = this;
  }

  public JDA getJdaContext() {
    return this.jdaContext;
  }

  public DiscordEventListener getEventListener() {
    return this.eventListener;
  }

  @Override
  public void run() {
    this.serviceStatus = new ServiceStatus();

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
    } catch (LoginException lex) {
      DiscordService.logger.error("Can't login to discord.");
    }
  }

  public static void initialize() {
    if (ConfigManager.getEnvManager().getBoolean(Env.ENV_DISABLE_DISCORD)) {
      logger.warn("Discord bot service has been disabled by default");
      return;
    }
    DiscordService.logger.info("Starting Discord service");
    DiscordService.serviceThread = new Thread(new DiscordService());
    serviceThread.start();
  }

  public static DiscordService getInstance() {
    return DiscordService.instance;
  }

  private JDA jdaContext;
  private DiscordEventListener eventListener;
  private ServiceStatus serviceStatus;

  private static Thread serviceThread;
  private static DiscordService instance;
  private static Logger logger;

}

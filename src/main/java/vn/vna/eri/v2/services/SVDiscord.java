package vn.vna.eri.v2.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.configs.CFDiscordEventListener;
import vn.vna.eri.v2.configs.CFGlobalConfig;
import vn.vna.eri.v2.error.ERDiscordServiceExists;
import vn.vna.eri.v2.schema.DCServiceStatus;

public class SVDiscord implements Runnable {

  private static final Logger logger;
  private static Thread serviceThread;
  private static SVDiscord instance;

  static {
    logger = LoggerFactory.getLogger(SVDiscord.class);
  }

  private final DCServiceStatus serviceStatus;
  private JDA jdaContext;
  private CFDiscordEventListener eventListener;

  public SVDiscord() {
    if (!Objects.isNull(SVDiscord.instance)) {
      throw new ERDiscordServiceExists();
    }
    this.serviceStatus = new DCServiceStatus();
    this.serviceStatus.setStatus(DCServiceStatus.STATUS_OFFLINE);
    this.serviceStatus.setLastStartUp(null);

    SVDiscord.instance = this;
  }

  public static void initialize() {
    if (CFGlobalConfig.getInstance().getBoolean(CFGlobalConfig.ENV_DISABLE_DISCORD).orElse(false)) {
      logger.warn("Discord bot service is disabled by default");
      return;
    }
    SVDiscord.logger.info("Starting Discord service");
    SVDiscord.serviceThread = new Thread(new SVDiscord());
    serviceThread.start();
  }

  public static SVDiscord getInstance() {
    return SVDiscord.instance;
  }

  public void requirePermission(List<Permission> permissions) {
    List<Permission> mismatch = new ArrayList<>();
  }

  public JDA getJdaContext() {
    return this.jdaContext;
  }

  public DCServiceStatus getStatus() {
    return this.serviceStatus;
  }

  public CFDiscordEventListener getEventListener() {
    return this.eventListener;
  }

  public SelfUser getSelfUser() {
    return this.jdaContext.getSelfUser();
  }

  public Member getSelfAsMember(Guild guild) {
    return guild.getMember(this.getSelfUser());
  }

  @Override
  public void run() {
    String token = CFGlobalConfig.getInstance().getString(CFGlobalConfig.ENV_BOT_TOKEN).orElse("");

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

    this.eventListener = new CFDiscordEventListener();

    JDABuilder jdaBuilder = JDABuilder.create(token, intents).addEventListeners(eventListener);

    try {
      this.jdaContext = jdaBuilder.build();

      // Update status
      this.serviceStatus.setStatus(DCServiceStatus.STATUS_ONLINE);
      this.serviceStatus.setLastStartUp(Instant.now().toString());
    } catch (LoginException lex) {
      SVDiscord.logger.error("Can't login to discord.");
      this.serviceStatus.setStatus(DCServiceStatus.STATUS_ERROR);
    }
  }

}

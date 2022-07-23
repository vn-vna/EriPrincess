package vn.vna.eri.v2.services;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_BANS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_EMOJIS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_INVITES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_REACTIONS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_TYPING;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_PRESENCES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_WEBHOOKS;
import static vn.vna.eri.v2.configs.CFGlobalConfig.ENV_DISABLE_DISCORD;
import static vn.vna.eri.v2.schema.DCServiceStatus.STATUS_OFFLINE;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.security.auth.login.LoginException;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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

  @Getter
  private DCServiceStatus status;
  @Getter
  private JDA jdaContext;
  private CFDiscordEventListener eventListener;

  public SVDiscord() {
    if (!Objects.isNull(SVDiscord.instance)) {
      throw new ERDiscordServiceExists();
    }
    this.status = new DCServiceStatus();
    this.status.setStatus(STATUS_OFFLINE);
    this.status.setLastStartUp(null);

    SVDiscord.instance = this;
  }

  public static void initialize() {
    if (CFGlobalConfig.getInstance().getBoolean(ENV_DISABLE_DISCORD).orElse(false)) {
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
    Collections.addAll(intents,
        GUILD_MESSAGES, GUILD_MEMBERS, GUILD_BANS,
        GUILD_EMOJIS, GUILD_INVITES, GUILD_MESSAGE_TYPING,
        GUILD_PRESENCES, GUILD_VOICE_STATES, GUILD_WEBHOOKS,
        GUILD_MESSAGE_REACTIONS);

    this.eventListener = CFDiscordEventListener.getInstance();

    JDABuilder jdaBuilder = JDABuilder.create(token, intents).addEventListeners(eventListener);

    try {
      this.jdaContext = jdaBuilder.build();

      // Update status
      this.status.setStatus(DCServiceStatus.STATUS_ONLINE);
      this.status.setLastStartUp(Instant.now().toString());
    } catch (LoginException lex) {
      SVDiscord.logger.error("Can't login to discord.");
      this.status.setStatus(DCServiceStatus.STATUS_ERROR);
    }
  }

}

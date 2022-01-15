package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.vna.erivampir.EriServer;
import vn.vna.erivampir.ServerConfig;
import vn.vna.erivampir.db.discordsvr.DiscordServerCfgRepoI;
import vn.vna.erivampir.db.ericfg.EriCfgRepoI;
import vn.vna.erivampir.discord.msgcmd.OnMessageListener;
import vn.vna.erivampir.discord.slash.OnSlashCommand;
import vn.vna.erivampir.discord.slash.PingSlashCommand;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
public class DiscordBotService {
    private static DiscordBotService instance;
    private final  Logger            logger = LoggerFactory.getLogger(DiscordBotService.class);
    protected      JDA               jdaClient;
    protected      OnMessageListener onMessageListener;
    protected      OnReadyListener   onReadyEvent;
    protected      OnSlashCommand    onSlashCommand;

    @Autowired
    private DiscordServerCfgRepoI discordServerCfgRepo;

    @Autowired
    private EriCfgRepoI eriCfgRepo;

    public static DiscordBotService getInstance() {
        synchronized (DiscordBotService.class) {
            if (Objects.isNull(instance)) {
                instance = EriServer.getAppContext().getBean(DiscordBotService.class);
            }
        }
        return instance;
    }

    public void awake(String[] args) {
        onMessageListener = new OnMessageListener();
        onReadyEvent      = new OnReadyListener();
        onSlashCommand    = new OnSlashCommand();

        String token = ServerConfig.getInstance().getConfiguration(ServerConfig.CFG_DISCORD_BOT_TOKEN);

        if (Objects.isNull(token)) {
            throw new IllegalArgumentException("Token String is null");
        }

        logger.info("Discord Bot Service has started");
        Collection<GatewayIntent> intents = new ArrayList<>();

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

        JDABuilder jdaBuilder = JDABuilder.create(token, intents);
        jdaBuilder
            .addEventListeners(onReadyEvent)
            .addEventListeners(onMessageListener)
            .addEventListeners(onSlashCommand)
            .setActivity(Activity.playing("with VNA"));

        try {
            jdaClient = jdaBuilder.build();
            jdaClient
                .upsertCommand(PingSlashCommand.COMMAND, PingSlashCommand.DESCRIPTION)
                .queue();
        } catch (LoginException lex) {
            lex.printStackTrace();
        }
    }

    public JDA getJDAClient() {
        return jdaClient;
    }

    public OnMessageListener getOnMessageListener() {
        return onMessageListener;
    }

    public OnReadyListener getOnReadyEventListener() {
        return onReadyEvent;
    }

    public OnSlashCommand getOnSlashCommandListener() {
        return onSlashCommand;
    }

    public DiscordServerCfgRepoI getDiscordServerCfgRepo() {
        return discordServerCfgRepo;
    }

    public EriCfgRepoI getEriCfgRepo() {
        return eriCfgRepo;
    }
}

package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.vna.erivampir.EriServer;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
public class DiscordBotService {
    private static DiscordBotService instance;
    private final  Logger            logger = LoggerFactory.getLogger(DiscordBotService.class);
    protected      JDABuilder        jdaBuilder;
    protected      JDA               jdaClient;
    protected      OnMessageListener messageListener;

    public static DiscordBotService getInstance() {
        synchronized (DiscordBotService.class) {
            if (Objects.isNull(instance)) {
                instance = EriServer.getAppContext().getBean(DiscordBotService.class);
            }
        }
        return instance;
    }

    public void awake(String[] args) {
        messageListener = new OnMessageListener();
        String token = null;

        // Find token in args
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith("--token")) {
                String[] tokenArg = args[i].split("=");
                if (tokenArg.length != 2) {
                    throw new IllegalArgumentException("Bad token argument");
                }
                token = tokenArg[1];
                break;
            }
        }

        if (Objects.isNull(token)) {
            throw new IllegalArgumentException("Token String is null");
        }

        logger.info("Discord Bot Service has started");
        Collection<GatewayIntent> intents = new ArrayList<>();
        intents.add(GatewayIntent.GUILD_MESSAGES);
        intents.add(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        jdaBuilder = JDABuilder.create(token, intents);
        jdaBuilder.addEventListeners(messageListener);

        try {
            jdaClient = jdaBuilder.build();
        } catch (LoginException lex) {
            lex.printStackTrace();
        }
    }

    public JDABuilder getJDABuilder() {
        return this.jdaBuilder;
    }

    public JDA getJDAClient() {
        return jdaClient;
    }
}

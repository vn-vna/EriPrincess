package vn.vna.erivampir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.vna.erivampir.discord.DiscordBotService;


@SpringBootApplication
public class EriServer {
    protected static ApplicationContext appCtx;
    protected static DiscordBotService  discordBotService;
    protected static ServerConfig       serverConfig;

    public static void main(String[] args) {
        serverConfig = new ServerConfig();
        serverConfig.parseArgs(args);

        appCtx = SpringApplication.run(EriServer.class, args);

        discordBotService = DiscordBotService.getInstance();
        discordBotService.awake(args);
    }

    public static ApplicationContext getAppContext() {
        return appCtx;
    }

    public static DiscordBotService getDiscordBotService() {
        return discordBotService;
    }

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }

}

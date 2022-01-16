package vn.vna.erivampir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.vna.erivampir.cli.CommandLineService;
import vn.vna.erivampir.discord.DiscordBotService;

@SpringBootApplication
public class EriServer {
    protected static ApplicationContext appCtx;

    public static void main(String[] args) {
        EriServerConfig.getInstance().parseArgs(args);
        appCtx = SpringApplication.run(EriServer.class, args);
        CommandLineService.getInstance();
        DiscordBotService.getInstance().awake(args);
    }

    public static ApplicationContext getAppContext() {
        return appCtx;
    }

}

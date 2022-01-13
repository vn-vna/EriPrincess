package vn.vna.erivampir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.vna.erivampir.discord.DiscordBotService;

@SpringBootApplication
public class EriServer {
    public static void main(String[] args) {
        ApplicationContext appCtx = SpringApplication.run(EriServer.class, args);
        DiscordBotService eriBot = appCtx.getBean(DiscordBotService.class);
        eriBot.awake(args);
    }
}

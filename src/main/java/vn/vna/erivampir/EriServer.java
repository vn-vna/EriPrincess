package vn.vna.erivampir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.vna.erivampir.cli.CommandLineService;
import vn.vna.erivampir.discord.DiscordBotService;

@SpringBootApplication
@EntityScan("vn.vna.erivampir")
@EnableJpaRepositories
@EnableCaching
@EnableScheduling
public class EriServer {
    protected static ApplicationContext appCtx;

    public static void main(String[] args) {
        System.out.println(System.getenv());
        EriServerConfig.getInstance().parseArgs(args);
        appCtx = SpringApplication.run(EriServer.class, args);
        CommandLineService.getInstance();
        DiscordBotService.getInstance().awake(args);
    }

    public static ApplicationContext getAppContext() {
        return appCtx;
    }

}

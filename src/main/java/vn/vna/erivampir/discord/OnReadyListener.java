package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.cli.CommandLineService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class OnReadyListener extends ListenerAdapter {

    Logger logger = LoggerFactory.getLogger(OnReadyListener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ListenerAdapter.class.getResourceAsStream("/console-banner"))));
            String         str    = "";
            while (!Objects.isNull(str = reader.readLine())) {
                logger.info(str);
            }
            reader.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        logger.info("Logged in successfully as [" + event.getJDA().getSelfUser().getAsTag() + "]");
        CommandLineService.getInstance().awake();
    }
}

package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class OnReadyEvent extends ListenerAdapter {

    Logger logger = LoggerFactory.getLogger(OnReadyEvent.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        logger.info("Logged in successfully as [" + event.getJDA().getSelfUser().getAsTag() + "]");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ListenerAdapter.class.getResourceAsStream("/console-banner")));
            String str = "";
            while (!Objects.isNull(str = reader.readLine())) {
                System.out.println(str);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
}

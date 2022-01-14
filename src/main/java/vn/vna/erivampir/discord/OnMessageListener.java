package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class OnMessageListener extends ListenerAdapter {
    protected Logger logger;

    public OnMessageListener() {
        logger = LoggerFactory.getLogger(OnMessageListener.class);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getMessage().getAuthor().isBot()) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith("E.")) {
            String[] command = event.getMessage().getContentRaw().substring(2).split(" ");
            if ("admin".equals(command[0])) {
                adminCommandEvaler(command);
                logger.info("Executed administrator command >> " + Arrays.toString(command));
            }
        }
    }

    public void adminCommandEvaler(String[] command) {

    }
}

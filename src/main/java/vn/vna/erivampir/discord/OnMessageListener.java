package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.EriServer;
import vn.vna.erivampir.ServerConfig;
import vn.vna.erivampir.discord.msgcmd.PingCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class OnMessageListener extends ListenerAdapter {
    protected Logger                     logger;
    protected Collection<MessageCommand> adminCommands;
    protected Collection<MessageCommand> msgCommands;
    protected String                     eriPrefix;

    public OnMessageListener() {
        logger        = LoggerFactory.getLogger(OnMessageListener.class);
        adminCommands = new ArrayList<>();
        msgCommands   = new ArrayList<>();
        eriPrefix     = EriServer.getServerConfig().getConfiguration(ServerConfig.CFG_ERIBOT_PREFIX);
        if ("".equals(eriPrefix)) {
            logger.error("Prefix can't be NULL or EMPTY");
            throw new IllegalStateException("Please configure a prefix");
        }

        // Add commands
        msgCommands.add(new PingCommand());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getMessage().getAuthor().isBot()) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith("E.")) {
            String[] commands = event.getMessage()
                .getContentRaw()
                .substring(eriPrefix.length())
                .split(" ");

            if ("admin".equals(commands[0])) {
                adminCommandEval(Arrays.copyOfRange(commands, 1, commands.length), event);
                logger.info("Executed administrator command >> " + Arrays.toString(commands));
            } else {
                messageCommandEval(commands, event);
            }
        }
    }

    public void adminCommandEval(String[] commands, MessageReceivedEvent event) {
        for (MessageCommand msgCommand : adminCommands) {
            if (!Objects.isNull(msgCommand) && msgCommand.matchCommand(commands, event)) {
                msgCommand.invoke(commands, event);
                break;
            }
        }
    }

    public void messageCommandEval(String[] commands, MessageReceivedEvent event) {
        logger.debug("Triggered message command evaler >> " + Arrays.toString(commands));
        for (MessageCommand msgCommand : msgCommands) {
            if (!Objects.isNull(msgCommand) && msgCommand.matchCommand(commands, event)) {
                msgCommand.invoke(commands, event);
                break;
            }
        }
    }
}

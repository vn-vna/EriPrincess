package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.discord.msgcmd.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class OnMessageListener extends ListenerAdapter {
    protected Logger logger;
    protected Collection<CommandTemplate> msgCommands;
    protected String eriPrefix;

    public OnMessageListener() {
        logger = LoggerFactory.getLogger(OnMessageListener.class);
        msgCommands = new ArrayList<>();
        eriPrefix = EriServerConfig.getInstance().getConfiguration(EriServerConfig.CFG_ERIBOT_PREFIX);
        if ("".equals(eriPrefix)) {
            logger.error("Prefix can't be NULL or EMPTY");
            throw new IllegalStateException("Please configure a prefix");
        }

        // === Add commands
        loadCommands(msgCommands);
    }

    private void loadCommands(Collection<CommandTemplate> commandsPool) {
        commandsPool.add(new HelpCommand());
        commandsPool.add(new ImgSearchCommand());
        commandsPool.add(new PingCommand());
        commandsPool.add(new PropertiesCommand());
        commandsPool.add(new RegisterCommand());
        commandsPool.add(new ScheduleMessageCommand());
        commandsPool.add(new UnregisterCommand());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMessage().getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(eriPrefix)) {
            logger.info("Received a command from guild [" + event.getGuild().getName() + "]: "
                    + event.getMessage().getContentRaw());
            String[] commands = event.getMessage().getContentRaw().substring(eriPrefix.length()).split(" ");
            messageCommandEval(commands, event);
        }
    }

    public void messageCommandEval(String[] commands, MessageReceivedEvent event) {
        logger.debug("Triggered message command evaler >> " + Arrays.toString(commands));
        for (CommandTemplate msgCommand : msgCommands) {
            if (!Objects.isNull(msgCommand) && msgCommand.matchCommand(commands, event)) {
                msgCommand.invoke(commands, event);
                break;
            }
        }
    }

    public Collection<CommandTemplate> getMsgCommands() {
        return msgCommands;
    }
}

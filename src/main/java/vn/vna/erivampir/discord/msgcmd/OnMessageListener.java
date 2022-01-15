package vn.vna.erivampir.discord.msgcmd;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.EriServer;
import vn.vna.erivampir.ServerConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class OnMessageListener extends ListenerAdapter {
    protected Logger                      logger;
    protected Collection<CommandTemplate> adminCommands;
    protected Collection<CommandTemplate> msgCommands;
    protected String                      eriPrefix;

    public OnMessageListener() {
        logger        = LoggerFactory.getLogger(OnMessageListener.class);
        adminCommands = new ArrayList<>();
        msgCommands   = new ArrayList<>();
        eriPrefix     = ServerConfig.getInstance().getConfiguration(ServerConfig.CFG_ERIBOT_PREFIX);
        if ("".equals(eriPrefix)) {
            logger.error("Prefix can't be NULL or EMPTY");
            throw new IllegalStateException("Please configure a prefix");
        }

        // === Add commands
        loadCommands(msgCommands, ".ncmd", CommandTemplate.NormalCommand.class);
        loadCommands(adminCommands, ".acmd", CommandTemplate.AdminCommand.class);
    }

    private void loadCommands(Collection<CommandTemplate> commandsPool, String pkg, Class<? extends Annotation> annotation) {
        Reflections   allCmdHandlers    = new Reflections(OnMessageListener.class.getPackageName() + pkg);
        Set<Class<?>> targetCmdHandlers = allCmdHandlers.getTypesAnnotatedWith(annotation);
        for (Class<?> targetCmdHandler : targetCmdHandlers) {
            try {
                final Object handler = targetCmdHandler.getDeclaredConstructor().newInstance();
                if (handler instanceof CommandTemplate targetCommand) {
                    commandsPool.add(targetCommand);
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                logger.error("Cannot create instance of command handler " + targetCmdHandler.getName() + " " + e.getMessage());
            }
        }
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
        for (CommandTemplate msgCommand : adminCommands) {
            if (!Objects.isNull(msgCommand) && msgCommand.matchCommand(commands, event)) {
                msgCommand.invoke(commands, event);
                break;
            }
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

    public Collection<CommandTemplate> getAdminCommands() {
        return adminCommands;
    }

    public Collection<CommandTemplate> getMsgCommands() {
        return msgCommands;
    }
}

package vn.vna.erivampir.discord.msgcmd;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.discord.MessageCommand;

public class PingCommand implements MessageCommand {

    public static final String COMMAND     = "ping";
    public static final String DESCRIPTION = "pong!";

    @Override
    public boolean matchCommand(String[] commands, MessageReceivedEvent event) {
        return COMMAND.equals(commands[0]);
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        event
            .getMessage()
            .getChannel()
            .sendMessage(
                new MessageBuilder()
                    .append("Hello")
                    .build()
            )
            .queue();
    }
}

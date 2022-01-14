package vn.vna.erivampir.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MessageCommand {

    boolean matchCommand(String[] commands, MessageReceivedEvent event);

    void invoke(String[] commands, MessageReceivedEvent event);
}

package vn.vna.erivampir.discord.msgcmd;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public abstract class CommandTemplate {

    protected String command;
    protected String description;

    public boolean matchCommand(String[] commands, MessageReceivedEvent event) {
        return !Objects.isNull(command) && command.equals(commands[0]);
    }

    public abstract void invoke(String[] commands, MessageReceivedEvent event);

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public @interface AdminCommand {
    }

    public @interface NormalCommand {
    }

}

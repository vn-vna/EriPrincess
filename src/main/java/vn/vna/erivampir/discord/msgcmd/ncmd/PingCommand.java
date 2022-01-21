package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.time.temporal.ChronoUnit;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class PingCommand extends CommandTemplate {

    public PingCommand() {
        super("ping", "Check ping");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        event.getMessage()
            .getChannel()
            .sendMessage("Execution ping checker..")
            .queue(m -> {
                long ping = event.getMessage().getTimeCreated().until(
                    m.getTimeCreated(), ChronoUnit.MILLIS);
                m.editMessage("Pong!!\nLatency: " + ping + "ms\nWebsocket: " +
                        event.getJDA().getGatewayPing() + "ms")
                    .queue();
            });
    }
}

package vn.vna.erivampir.discord.msgcmd;

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
                .queue(message -> {
                    long ping = event.getMessage().getTimeCreated().until(
                            message.getTimeCreated(), ChronoUnit.MILLIS);
                    String pingMsg = """
                            Eri pong!!!
                            ```
                            Message latency:  %dms
                            Gateway ping:     %dms
                            ```
                            """.formatted(ping, event.getJDA().getGatewayPing());
                    message
                            .editMessage(pingMsg)
                            .queue();

                });
    }
}

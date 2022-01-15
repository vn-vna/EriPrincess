package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.db.discordsvr.DiscordServerCfgRepoI;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.time.temporal.ChronoUnit;

@CommandTemplate.NormalCommand
public class PingCommand extends CommandTemplate {

    protected DiscordServerCfgRepoI serverCfgRepo;

    public PingCommand() {
        super("ping", "Check ping");
        serverCfgRepo = DiscordBotService.getInstance().getDiscordServerCfgRepo();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        event.getMessage().getChannel().sendMessage("Execution ping checker..").queue(m -> {
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("Pong!!\nLatency: " + ping + "ms\nWebsocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }
}

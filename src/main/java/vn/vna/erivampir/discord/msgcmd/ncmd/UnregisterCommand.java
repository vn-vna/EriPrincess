package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.db.discordsvr.DiscordServerCfgRepoI;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class UnregisterCommand extends CommandTemplate {

    DiscordServerCfgRepoI discordServerCfgRepo;
    Logger                logger = LoggerFactory.getLogger(UnregisterCommand.class);

    public UnregisterCommand() {
        super("unregister", "Unregister this server.");
        discordServerCfgRepo = DiscordBotService.getInstance().getDiscordServerCfgRepo();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        Guild discordServer = event.getGuild();
        try {
            discordServerCfgRepo.deleteById(discordServer.getId());
            event.getChannel().sendMessage("Server " + discordServer.getName() + " has no longer been registered").queue();
        } catch (Exception ex) {
            event.getChannel().sendMessage("Server " + discordServer.getName() + " has not been registered before").queue();
        }
    }
}

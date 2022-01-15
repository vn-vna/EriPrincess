package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import vn.vna.erivampir.db.discordsvr.DiscordServerCfgRepoI;
import vn.vna.erivampir.db.discordsvr.DiscordServerConfiguration;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.util.Date;

@CommandTemplate.NormalCommand
public class RegisterCommand extends CommandTemplate {

    DiscordServerCfgRepoI discordServerCfgRepo;
    Logger                logger = LoggerFactory.getLogger(RegisterCommand.class);

    public RegisterCommand() {
        super("register", "Register this server");
        discordServerCfgRepo = DiscordBotService.getInstance().getDiscordServerCfgRepo();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        Guild                      discordServer = event.getGuild();
        DiscordServerConfiguration dsc           = new DiscordServerConfiguration();
        dsc.setServerID(discordServer.getId());
        dsc.setRegisteredTime(new Date());
        dsc.setServerGMT(7);
        event.getChannel().sendMessage("Operation in progress...").queue((m) -> {
            try {
                discordServerCfgRepo.insert(dsc);
                m.editMessage("Register successfully").queue();
            } catch (DuplicateKeyException ex) {
                logger.error("Register operation FAILED from discord server " + discordServer.getName());
                m.editMessage("Register Failed").queue();
            }
        });
    }
}

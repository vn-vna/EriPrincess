package vn.vna.erivampir.discord.msgcmd.acmd;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import vn.vna.erivampir.db.ericfg.EriCfgRepoI;
import vn.vna.erivampir.db.ericfg.EriConfiguration;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.util.Date;
import java.util.Optional;

@CommandTemplate.AdminCommand
public class SetProperties extends CommandTemplate {

    EriCfgRepoI eriCfgRepo;
    Logger      logger = LoggerFactory.getLogger(SetProperties.class);

    public SetProperties() {
        super("set", "Set property for Eri");
        eriCfgRepo = DiscordBotService.getInstance().getEriCfgRepo();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        if (commands.length != 3) {
            event.getMessage().reply("Invalid arguments").queue();
            return;
        }
        event.getMessage().reply("Executing admin command").queue((m) -> {
            try {
                eriCfgRepo.insert(new EriConfiguration(commands[1], commands[2], new Date()));
            } catch (DuplicateKeyException ex) {
                Optional<EriConfiguration> eriConfiguration = eriCfgRepo.findById(commands[1]);
                if (eriConfiguration.isPresent()) {
                    eriConfiguration.get().setLastModify(new Date());
                    eriConfiguration.get().setValue(commands[2]);
                }
            }
            m.editMessage("Execution successfully").queue();
        });

    }
}

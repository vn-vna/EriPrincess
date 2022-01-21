package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import vn.vna.erivampir.db.h2.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.db.h2.dscguild.DscGuildCfgRepository;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class UnregisterCommand extends CommandTemplate {

    //    DiscordServerCfgRepoI discordServerCfgRepoMG;
    DscGuildCfgRepository dscGuildCfgRepositoryH2;
    Logger                logger = LoggerFactory.getLogger(UnregisterCommand.class);

    public UnregisterCommand() {
        super("unregister", "Unregister this server.");
//        discordServerCfgRepoMG = DiscordBotService.getInstance().getDiscordServerCfgRepoMG();
        dscGuildCfgRepositoryH2 = DiscordBotService.getInstance().getDscGuildCfgRepositoryH2();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        Guild discordGuild = event.getGuild();
//            discordServerCfgRepoMG.deleteById(discordServer.getId());

        event
            .getMessage()
            .reply("Executing request")
            .queue((messageAction) -> {
                DiscordGuildConfig example = new DiscordGuildConfig();
                example.setGuildId(discordGuild.getId());
                Optional<DiscordGuildConfig> resultFindGuild = dscGuildCfgRepositoryH2
                    .findOne(Example.of(example));

                if (resultFindGuild.isPresent()) {
                    dscGuildCfgRepositoryH2.delete(resultFindGuild.get());
                    messageAction
                        .editMessage("Server " + discordGuild.getName() + " has no longer been registered")
                        .queue();
                } else {
                    messageAction
                        .editMessage("Server " + discordGuild.getName() + " has not been registered before")
                        .queue();
                }
            });

    }
}

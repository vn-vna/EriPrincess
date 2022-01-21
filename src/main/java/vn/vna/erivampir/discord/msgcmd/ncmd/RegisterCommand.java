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

import java.util.Date;
import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class RegisterCommand extends CommandTemplate {

    DscGuildCfgRepository dscGuildCfgRepositoryH2;
    Logger                logger = LoggerFactory.getLogger(RegisterCommand.class);

    public RegisterCommand() {
        super("register", "Register this server");
        dscGuildCfgRepositoryH2 =
            DiscordBotService.getInstance().getDscGuildCfgRepositoryH2();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        Guild discordGuild = event.getGuild();

        event.getMessage().reply("Executing request").queue((messageAction) -> {
            DiscordGuildConfig example = new DiscordGuildConfig();
            example.setGuildId(discordGuild.getId());

            Optional<DiscordGuildConfig> resultFindGuild =
                dscGuildCfgRepositoryH2.findOne(Example.of(example));

            if (resultFindGuild.isPresent()) {
                messageAction
                    .editMessage("Server " + discordGuild.getName() +
                        " has been registered before.")
                    .queue();
            } else {
                example.setRegisteredDate(new Date());
                example.setGmt(7);
                dscGuildCfgRepositoryH2.save(example);
                messageAction
                    .editMessage("Server " + discordGuild.getName() +
                        " has not been registered successfully.")
                    .queue();
            }
        });
    }
}

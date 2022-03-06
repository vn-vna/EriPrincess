package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.util.Date;
import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class RegisterCommand extends CommandTemplate {

    Logger                logger = LoggerFactory.getLogger(RegisterCommand.class);

    public RegisterCommand() {
        super("register", "Register this server");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        // PSEUDO
    }
}

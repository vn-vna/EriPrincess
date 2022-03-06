package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfigRepository;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class PropertiesCommand extends CommandTemplate {

    private DiscordGuildConfigRepository discordGuildConfigRepository;

    public PropertiesCommand() {
        super("props", "Show properties of this server");

        discordGuildConfigRepository = DiscordBotService.getInstance().getDiscordGuildConfigRepository();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        Optional<DiscordGuildConfig> guildConfig = DiscordUtilities.findGuildById(event.getGuild().getId());
        if (guildConfig.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.append("Server is not registered");

        }
    }
}

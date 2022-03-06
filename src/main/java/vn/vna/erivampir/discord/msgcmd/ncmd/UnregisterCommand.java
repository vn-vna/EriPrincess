package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class UnregisterCommand extends CommandTemplate {

    Logger                logger = LoggerFactory.getLogger(UnregisterCommand.class);

    public UnregisterCommand() {
        super("unregister", "Unregister this server.");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        boolean authorIsAdmin = false;

        List<Role> roles = Objects.requireNonNull(event.getGuild().getMember(event.getAuthor())).getRoles();
        for (var role : roles) {
            if (role.hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_SERVER, Permission.MANAGE_ROLES)) {
                authorIsAdmin = true;
            }
        }

        if (!authorIsAdmin) {
            EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
            embedBuilder
                .setTitle("User do not have permission to do this action \uD83D\uDE05")
                .setDescription("Please contact administrator");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder
                .setEmbeds(embedBuilder.build());

            event
                .getChannel()
                .sendMessage(messageBuilder.build())
                .queue();

            return;
        }

        event
            .getChannel()
            .sendMessage("Parsing Request")
            .queue(message -> {
                Optional<DiscordGuildConfig> fGuildConfig = DiscordUtilities.findGuildById(event.getGuild().getId());
                if (fGuildConfig.isEmpty()) {
                    EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
                    embedBuilder
                        .setTitle("Guild has not been registered before \uD83E\uDD17")
                        .setDescription("You can't unregister this guild now");

                    MessageBuilder messageBuilder = new MessageBuilder();
                    messageBuilder
                        .setEmbeds(embedBuilder.build());

                    event
                        .getChannel()
                        .sendMessage(messageBuilder.build())
                        .queue();

                } else {
                    DiscordUtilities.unregisterGuildToDb(event.getGuild().getId());

                    EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
                    embedBuilder
                        .setTitle("Guild has been unregistered successfully \uD83D\uDE14")
                        .setDescription("Hope to see you again \uD83D\uDE15");

                    MessageBuilder messageBuilder = new MessageBuilder();
                    messageBuilder
                        .setEmbeds(embedBuilder.build());

                    message
                        .editMessage(messageBuilder.build())
                        .queue();
                }
            });
    }
}

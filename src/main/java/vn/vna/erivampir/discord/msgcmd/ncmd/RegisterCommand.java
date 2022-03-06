package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class RegisterCommand extends CommandTemplate {

    Logger logger = LoggerFactory.getLogger(RegisterCommand.class);

    public RegisterCommand() {
        super("register", "Register this server");
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
                if (fGuildConfig.isPresent()) {
                    EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
                    embedBuilder
                        .setTitle("Guild has been registered before \uD83E\uDD17")
                        .setDescription("You no need to do it again");

                    MessageBuilder messageBuilder = new MessageBuilder();
                    messageBuilder
                        .setEmbeds(embedBuilder.build());

                    event
                        .getChannel()
                        .sendMessage(messageBuilder.build())
                        .queue();

                } else {
                    DiscordUtilities.registerGuildToDb(event.getGuild().getId());

                    EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
                    embedBuilder
                        .setTitle("Guild has been registered successfully \uD83E\uDD17")
                        .setDescription("I'm glad to see you here \uD83D\uDE1C");

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

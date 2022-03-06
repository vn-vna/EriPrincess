package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.Objects;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class HelpCommand extends CommandTemplate {

    public HelpCommand() {
        super("help", "Show this message");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
        final String version      = EriServerConfig.ERI_VERSION;

        embedBuilder
            .setTitle("I'm Eri, the princess of vampire empire ~~")
            .setDescription("Let's see what i can do \uD83D\uDC4B.") // \uD83D\uDC4b = 👋
            .addBlankField(false)
            .addField("Message Commands", "Command with no authorizer", false);
        for (CommandTemplate command : DiscordBotService.getInstance().getOnMessageListener().getMsgCommands()) {
            if (!Objects.isNull(command)) {
                embedBuilder.addField(command.getCommand(), command.getDescription(), true);
            }
        }

        event.getMessage().getChannel().sendMessage("Fetching data").queue((m) -> {
            embedBuilder
                .setFooter(version);
            MessageBuilder msgBuilder = new MessageBuilder();
            msgBuilder.setEmbeds(embedBuilder.build());
            msgBuilder.setContent(null);
            m.editMessage(event.getAuthor().getAsMention()).queue();
            m.editMessage(msgBuilder.build()).queue();
        });
    }
}

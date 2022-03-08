package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.Objects;

import static vn.vna.erivampir.utilities.DiscordUtilities.ZERO_WIDTH_SPACE;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class HelpCommand extends CommandTemplate {

    public HelpCommand() {
        super("help", "Show this message");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {

        event
            .getMessage()
            .reply("Fetching data")
            .mentionRepliedUser(false)
            .queue((message) -> {
                MessageBuilder msgBuilder   = new MessageBuilder();
                EmbedBuilder   embedBuilder = DiscordUtilities.getEriEmbedBuilder();

                embedBuilder
                    .setTitle("I'm Eri, the princess of vampire empire ~~")
                    .setDescription("Let's see what i can do \uD83D\uDC4B.") // \uD83D\uDC4b = 👋
                    .addBlankField(false)
                    .addField("Message Commands", "Eri is listening to you", false);
                for (CommandTemplate command : DiscordBotService.getInstance().getOnMessageListener().getMsgCommands()) {
                    if (!Objects.isNull(command)) {
                        embedBuilder.addField(command.getCommand(), command.getDescription(), true);
                    }
                }

                msgBuilder.setEmbeds(embedBuilder.build());
                msgBuilder.setContent(null);
                message
                    .editMessage(ZERO_WIDTH_SPACE)
                    .mentionRepliedUser(false)
                    .queue();
                message
                    .editMessage(msgBuilder.build())
                    .mentionRepliedUser(false)
                    .queue();
            });
    }
}

package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.awt.Color;
import java.util.Objects;

@CommandTemplate.NormalCommand
public class HelpCommand extends CommandTemplate {

    public HelpCommand() {
        super("help", "Show this message");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final String version      = EriServerConfig.ERI_VERSION;

        embedBuilder
            .setColor(Color.RED)
            .setTitle("Eri help central.")
            .setThumbnail("https://i.imgur.com/sTDFv85.png")
            .setDescription("Let's see what i can do \uD83D\uDC4B.") // \uD83D\uDC4b = 👋
            .addBlankField(false)
            .addField("Message Commands", "Command with no authorizer", false);
        for (CommandTemplate command : DiscordBotService.getInstance().getOnMessageListener().getMsgCommands()) {
            if (!Objects.isNull(command)) {
                embedBuilder.addField(command.getCommand(), command.getDescription(), true);
            }
        }

//        embedBuilder
//            .addField("Administrator Commands", "Privileged commands", false);
//        for (CommandTemplate command : DiscordBotService.getInstance().getOnMessageListener().getAdminCommands()) {
//            if (!Objects.isNull(command)) {
//                embedBuilder.addField(command.getCommand(), command.getDescription(), true);
//            }
//        }

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

package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.ServerConfig;
import vn.vna.erivampir.db.discordsvr.DiscordServerCfgRepoI;
import vn.vna.erivampir.db.discordsvr.DiscordServerConfiguration;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

import java.awt.Color;
import java.util.Objects;
import java.util.Optional;

@CommandTemplate.NormalCommand
public class PropertiesCommand extends CommandTemplate {

    DiscordServerCfgRepoI discordServerCfgRepo;

    public PropertiesCommand() {
        super("props", "Show properties of this server");
        discordServerCfgRepo = DiscordBotService.getInstance().getDiscordServerCfgRepo();
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        Optional<DiscordServerConfiguration> serverConfiguration = discordServerCfgRepo.findById(event.getGuild().getId());
        MessageBuilder                       messageBuilder      = new MessageBuilder();
        EmbedBuilder                         embedBuilder        = new EmbedBuilder();
        final String                         version             = ServerConfig.ERI_VERSION;

        embedBuilder
            .setTitle("Server register properties")
            .setThumbnail("https://i.imgur.com/sTDFv85.png")
            .setColor(Color.RED)
            .setFooter(version);

        if (serverConfiguration.isPresent()) {
            embedBuilder
                .addField("Server ID", serverConfiguration.get().getServerID(), false)
                .addField("Registered on", serverConfiguration.get().getRegisteredTime().toString(), false)
                .addField("Server timezone", serverConfiguration.get().getServerGMT().toString(), false);
        } else {
            embedBuilder
                .addField("❌ Server has not been registered yet", "Sorry about that", false);
        }
        messageBuilder
            .setContent("Here you are")
            .setEmbeds(embedBuilder.build());

        event.getChannel().sendMessage("Waiting...").queue((m) -> {
            m.editMessage(messageBuilder.build()).queue();
        });
    }
}

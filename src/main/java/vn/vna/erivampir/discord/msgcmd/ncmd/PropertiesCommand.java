package vn.vna.erivampir.discord.msgcmd.ncmd;

import java.awt.Color;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.data.domain.Example;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.db.h2.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.db.h2.dscguild.DscGuildCfgRepository;
import vn.vna.erivampir.discord.DiscordBotService;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class PropertiesCommand extends CommandTemplate {

  DscGuildCfgRepository dscGuildCfgRepositoryH2;

  public PropertiesCommand() {
    super("props", "Show properties of this server");
    dscGuildCfgRepositoryH2 =
        DiscordBotService.getInstance().getDscGuildCfgRepositoryH2();
  }

  @Override
  public void invoke(String[] commands, MessageReceivedEvent event) {
    DiscordGuildConfig example = new DiscordGuildConfig();
    example.setGuildId(event.getGuild().getId());
    Optional<DiscordGuildConfig> guildConfigSR =
        dscGuildCfgRepositoryH2.findOne(Example.of(example));

    MessageBuilder messageBuilder = new MessageBuilder();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    final String version = EriServerConfig.ERI_VERSION;

    embedBuilder.setTitle("Server register properties")
        .setThumbnail("https://i.imgur.com/sTDFv85.png")
        .setColor(Color.RED)
        .setFooter(version);

    if (guildConfigSR.isPresent()) {
      embedBuilder
          .addField("Server ID", guildConfigSR.get().getGuildId(), false)
          .addField("Registered on",
                    guildConfigSR.get().getRegisteredDate().toString(), false)
          .addField("Server timezone", guildConfigSR.get().getGmt().toString(),
                    false);
    } else {
      embedBuilder.addField("Server has not been registered yet",
                            "Sorry about that", false);
    }
    messageBuilder.setContent("Here you are").setEmbeds(embedBuilder.build());

    event.getChannel().sendMessage("Waiting...").queue((m) -> {
      m.editMessage(messageBuilder.build()).queue();
    });
  }
}

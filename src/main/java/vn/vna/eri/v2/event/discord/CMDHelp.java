package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.configs.CFLangPack.DEFAULT_LANG_PACK;
import static vn.vna.eri.v2.configs.CFLangPack.SECTION_TEMPLATE;
import static vn.vna.eri.v2.event.discord.helper.CommandType.MESSAGE_COMMAND;
import static vn.vna.eri.v2.utils.helper.PlaceholderEntry.entry;

import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.schema.DCGuildConfig;
import vn.vna.eri.v2.utils.UTMessageBuilder;

@CommandProperties(
  commands = "help",
  type = MESSAGE_COMMAND,
  descriptionKey = "cmd.desc.cmd-help")
public class CMDHelp
  extends CMDTemplate {

  public static final String LPK_TEMPLATE_TITLE = "tpl.help.title";

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent msgEvent) {
      MessageBuilder msgBuilder   = new MessageBuilder();
      EmbedBuilder   embedBuilder = UTMessageBuilder.getInstance().getDefaultEmbedBuilder();
      String         guildId      = msgEvent.getGuild().getId();

      Optional<DCGuildConfig> guildConfig = CLDiscordGuildConfig.getClient()
        .getConfiguration(guildId);

      CFLangPack.getInstance()
        .getLangPack(guildConfig.map((cfg) -> cfg.getLanguage())
          .orElse(DEFAULT_LANG_PACK.getName()))
        .ifPresent((langPack) -> {

          String templateTitle = langPack.get(SECTION_TEMPLATE, LPK_TEMPLATE_TITLE);
          String templateElem = langPack.get(SECTION_TEMPLATE, LPK_HELP_CHILD_PROPS);

          StringBuilder stringBuilder = new StringBuilder();

          for (CMDTemplate cmd : CMDTemplate.getCommandManager()) {
            String desc = langPack.get(CFLangPack.SECTION_CMD, cmd.getDescriptionKey());
            stringBuilder
              .append(UTMessageBuilder.getInstance().formatMessage(templateElem,
                entry("cmds", String.join(",", cmd.getCommands())),
                entry("description", desc)));
          }

          embedBuilder
            .addField(templateTitle, stringBuilder.toString(), false);
        });

      msgBuilder.setEmbeds(embedBuilder.build());
      msgEvent.getChannel()
        .sendMessage(msgBuilder.build())
        .queue();
    }
  }

}

package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.configs.CFLangPack.SECTION_TEMPLATE;
import static vn.vna.eri.v2.event.discord.helper.CommandType.MESSAGE_COMMAND;
import static vn.vna.eri.v2.utils.helper.PlaceholderEntry.entry;

import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.ini4j.Ini;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.utils.UTMessageBuilder;

@CommandProperties(
    type = MESSAGE_COMMAND,
    commands = "admin",
    descriptionKey = "cmd.desc.cmd-admin")
public class CMDAdmin
    extends CMDTemplate {

  public static final String LPK_TEMPLATE_TITLE = "tpl.help.title";
  public static final String LPK_TEMPLATE_DESC  = "tpl.help.desc";

  @Override
  public void execute(String[] commandList, Event event, Integer commandDepth) {
    if (event instanceof MessageReceivedEvent msgEvent) {
      String langPackName = CLDiscordGuildConfig.getClient()
          .getConfiguration(msgEvent.getGuild().getId())
          .map((cfg) -> cfg.getLanguage())
          .orElse(CFLangPack.DEFAULT_LANG_PACK.getName());

      Optional<Ini> langPack = CFLangPack.getInstance()
          .getLangPack(langPackName);

      String helpString  = this.getHelpString(langPackName);
      String descString  = langPack
          .map((pack) -> pack.get(SECTION_TEMPLATE, LPK_TEMPLATE_DESC))
          .orElse("");
      String titleString = langPack
          .map((pack) -> pack.get(SECTION_TEMPLATE, LPK_TEMPLATE_TITLE))
          .orElse("");

      EmbedBuilder   embed = UTMessageBuilder.getInstance().getDefaultEmbedBuilder();
      MessageBuilder msg   = new MessageBuilder();

      embed
          .setDescription(UTMessageBuilder
              .getInstance()
              .formatMessage(descString,
                  entry("cmd", String.join(",", this.getCommands()))))
          .addField(titleString, helpString, false);

      msg.setEmbeds(embed.build());
      msgEvent.getChannel()
          .sendMessage(msg.build())
          .queue();

    }
  }

}

package vn.vna.erivampir.discord.msgcmd.ncmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.discord.msgcmd.CommandTemplate;
import vn.vna.erivampir.utilities.DiscordUtilities;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
@CommandTemplate.NormalCommand
public class PropertiesCommand extends CommandTemplate {

    public PropertiesCommand() {
        super("props", "Show properties of this guild");
    }

    @Override
    public void invoke(String[] commands, MessageReceivedEvent event) {
        String                       guildName   = event.getGuild().getName();
        Optional<DiscordGuildConfig> guildConfig = DiscordUtilities.findGuildById(event.getGuild().getId());
        if (guildConfig.isEmpty()) {
            EmbedBuilder embedBuilder = DiscordUtilities.getEriEmbedBuilder();
            embedBuilder
                .setTitle("Guild [%s] has not been registered yet \uD83E\uDD14".formatted(guildName))
                .setDescription("Contact guild administrator to more information")
                .setImage(event.getGuild().getIconUrl());

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(embedBuilder.build());

            event
                .getMessage()
                .reply(messageBuilder.build())
                .mentionRepliedUser(false)
                .queue();
        } else {
            DiscordGuildConfig config       = guildConfig.get();
            EmbedBuilder       embedBuilder = DiscordUtilities.getEriEmbedBuilder();

            String guildGMT = (config.getGuildGMT() > 0 ? "+" : "-") + config.getGuildGMT();
            Member owner = event.getGuild().getOwner();
            String ownerName;

            if (Objects.isNull(owner)) {
                ownerName = "Unknown";
            } else {
                ownerName = owner.getNickname();
                if (Objects.isNull(ownerName) || "".equals(ownerName)) {
                    ownerName = owner.getEffectiveName();
                }
            }

            embedBuilder
                .setTitle("Properties of guild " + guildName)
                .addField("Guild ID", config.getGuildId(), true)
                .addField("Guild Owner", ownerName, true)
                .addField("Guild time zone", guildGMT, true)
                .addField("Guild registration date", config.getGuildRegisteredDate().toString(), true)
                .addBlankField(false)
                .addField("G.Morning message", config.isEnableGoodMorning() ? "Enabled" : "Disabled", true)
                .addField("G.Night message", config.isEnableGoodNight() ? "Enabled" : "Disabled", true)
                .addField("Lonely message", config.isEnableLonelyMessage() ? "Enabled" : "Disabled", true);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder
                .setEmbeds(embedBuilder.build());

            event
                .getChannel()
                .sendMessage(messageBuilder.build())
                .queue();
        }
    }
}

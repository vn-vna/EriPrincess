package vn.vna.erivampir.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfigRepository;
import vn.vna.erivampir.db.pgsql.ercfg.EriConfig;
import vn.vna.erivampir.db.pgsql.ercfg.EriConfigRepository;
import vn.vna.erivampir.discord.DiscordBotService;

import java.awt.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DiscordUtilities {
    public static final String ZERO_WIDTH_SPACE = "\u200B";

    public static Optional<DiscordGuildConfig> findGuildById(String guildId) {
        DiscordGuildConfigRepository discordGuildConfigRepository = DiscordBotService.getInstance().getDiscordGuildConfigRepository();
        DiscordGuildConfig           guildConfigExample           = new DiscordGuildConfig();
        guildConfigExample.setGuildId(guildId);
        Example<DiscordGuildConfig> findGuildExample = Example.of(guildConfigExample);
        return discordGuildConfigRepository.findOne(findGuildExample);
    }

    public static void registerGuildToDb(String guildId) {
        DiscordGuildConfigRepository discordGuildConfigRepository = DiscordBotService.getInstance().getDiscordGuildConfigRepository();
        DiscordGuildConfig           newConfig                    = new DiscordGuildConfig();
        newConfig.setGuildId(guildId);
        newConfig.setGuildGMT(7);
        newConfig.setGuildRegisteredDate(Timestamp.from(new Date().toInstant()));
        newConfig.setEnableGoodMorning(false);
        newConfig.setEnableGoodNight(false);
        newConfig.setEnableLonelyMessage(false);
        discordGuildConfigRepository.save(newConfig);
    }

    public static void unregisterGuildToDb(String guildId) {
        DiscordGuildConfigRepository discordGuildConfigRepository = DiscordBotService.getInstance().getDiscordGuildConfigRepository();
        DiscordGuildConfig           deleteConfig                 = new DiscordGuildConfig();
        deleteConfig.setGuildId(guildId);
        Example<DiscordGuildConfig>  deleteConfigExample = Example.of(deleteConfig);
        Optional<DiscordGuildConfig> config              = discordGuildConfigRepository.findOne(deleteConfigExample);
        if (config.isEmpty()) {
            return;
        }
        discordGuildConfigRepository.delete(config.get());
    }

    public static EmbedBuilder getEriEmbedBuilder() {
        Optional<EriConfig> versionConfig = getEriConfigFromDb("ERI_VERSION");
        String              versionString;
        if (versionConfig.isEmpty()) {
            versionString = EriServerConfig.ERI_VERSION;
        } else {
            versionString = versionConfig.get().getConfigValue();
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
            .setAuthor("Eri Vampir Shirone")
            .setThumbnail("https://i.imgur.com/sTDFv85.png")
            .setColor(Color.RED)
            .setFooter(versionString);
        return embedBuilder;
    }

    public static Optional<EriConfig> getEriConfigFromDb(String key) {
        EriConfigRepository eriConfigRepository = DiscordBotService.getInstance().getEriConfigRepository();
        EriConfig           eriConfig           = new EriConfig();
        eriConfig.setConfigKey(key);
        Example<EriConfig> eriConfigExample = Example.of(eriConfig);
        return eriConfigRepository.findOne(eriConfigExample);
    }

    public static boolean isFullPermission(Member member, Permission... permission) {
        boolean reqPermission = false;

        List<Role> roles = Objects.requireNonNull(member).getRoles();
        for (var role : roles) {
            if (role.hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_SERVER, Permission.MANAGE_ROLES)) {
                reqPermission = true;
            }
        }

        return reqPermission;
    }

    public static void replyUserNotEnoughPermission(@NotNull MessageReceivedEvent event) {
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
    }

}

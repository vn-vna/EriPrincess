package vn.vna.erivampir.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.data.domain.Example;
import vn.vna.erivampir.EriServerConfig;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfig;
import vn.vna.erivampir.db.pgsql.dscguild.DiscordGuildConfigRepository;
import vn.vna.erivampir.db.pgsql.ercfg.EriConfig;
import vn.vna.erivampir.db.pgsql.ercfg.EriConfigRepository;
import vn.vna.erivampir.discord.DiscordBotService;

import java.awt.*;
import java.util.Optional;

public class DiscordUtilities {
    public static Optional<DiscordGuildConfig> findGuildById(String guildId) {
        DiscordGuildConfigRepository discordGuildConfigRepository = DiscordBotService.getInstance().getDiscordGuildConfigRepository();
        DiscordGuildConfig           guildConfigExample           = new DiscordGuildConfig();
        guildConfigExample.setGuildId(guildId);
        Example<DiscordGuildConfig> findGuildExample = Example.of(guildConfigExample);
        return discordGuildConfigRepository.findOne(findGuildExample);
    }

    public static EmbedBuilder getEriEmbedBuilder() {
        Optional<EriConfig> versionConfig = getEriConfigFromDb("ERI_VERSION");
        String versionString;
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
        EriConfig eriConfig = new EriConfig();
        eriConfig.setConfigKey(key);
        Example<EriConfig> eriConfigExample = Example.of(eriConfig);
        return eriConfigRepository.findOne(eriConfigExample);
    }
}
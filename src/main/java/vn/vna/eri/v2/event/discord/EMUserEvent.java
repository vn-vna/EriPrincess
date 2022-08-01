package vn.vna.eri.v2.event.discord;

import static vn.vna.eri.v2.configs.CFLangPack.SECTION_TEMPLATE;
import static vn.vna.eri.v2.utils.helper.PlaceholderEntry.entry;

import java.util.Objects;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdatePendingEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.configs.CFLangPack;
import vn.vna.eri.v2.utils.UTMessageBuilder;

public class EMUserEvent
    extends ListenerAdapter {

  public static final String LPK_DEFAULT_AIRPORT_WELCOME = "tpl.default.airport-msg.welcome";
  public static final String LPK_DEFAULT_AIRPORT_GOODBYE = "tpl.default.airport-msg.goodbye";

  private static EMUserEvent instance;

  public static EMUserEvent getInstance() {
    synchronized (EMUserEvent.class) {
      if (Objects.isNull(EMUserEvent.instance)) {
        EMUserEvent.instance = new EMUserEvent();
      }
    }

    return EMUserEvent.instance;
  }

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    super.onGuildMemberJoin(event);

    CLDiscordGuildConfig cfgClient   = CLDiscordGuildConfig.getClient();
    CFLangPack           langPackMng = CFLangPack.getInstance();
    UTMessageBuilder     msgBuilder  = UTMessageBuilder.getInstance();

    Guild  guild   = event.getGuild();
    String guildId = guild.getId();
    Member member  = event.getMember();

    cfgClient
        .getConfiguration(guildId)
        .ifPresent((cfg) -> {
          String airportId = cfg.getAirportChannel();
          if (Objects.isNull(airportId)) {
            return;
          }

          TextChannel airportChannel = guild.getTextChannelById(airportId);

          if (Objects.isNull(airportChannel)) {
            return;
          }

          String tplWelcome = langPackMng.getString(cfg.getLanguage(),
              SECTION_TEMPLATE, LPK_DEFAULT_AIRPORT_WELCOME).orElse("");

          airportChannel
              .sendMessage(msgBuilder.formatMessage(tplWelcome,
                  entry("member", member.getAsMention()),
                  entry("guild", guild.getName()),
                  entry("no", guild.getMemberCount())))
              .queue();
        });
  }

  @Override
  public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
    super.onGuildMemberRemove(event);

    CLDiscordGuildConfig cfgClient   = CLDiscordGuildConfig.getClient();
    CFLangPack           langPackMng = CFLangPack.getInstance();
    UTMessageBuilder     msgBuilder  = UTMessageBuilder.getInstance();

    Guild  guild   = event.getGuild();
    String guildId = guild.getId();
    Member member  = event.getMember();

    cfgClient
        .getConfiguration(guildId)
        .ifPresent((cfg) -> {
          String airportId = cfg.getAirportChannel();
          if (Objects.isNull(airportId)) {
            return;
          }

          TextChannel airportChannel = guild.getTextChannelById(airportId);

          if (Objects.isNull(airportChannel)) {
            return;
          }

          String tplGoodbye = langPackMng.getString(cfg.getLanguage(),
              SECTION_TEMPLATE, LPK_DEFAULT_AIRPORT_GOODBYE).orElse("");

          airportChannel
              .sendMessage(msgBuilder.formatMessage(tplGoodbye,
                  entry("member", member.getEffectiveName()),
                  entry("guild", guild.getName()),
                  entry("count", guild.getMemberCount())))
              .queue();
        });
  }

  @Override
  public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
    super.onGuildMemberRoleAdd(event);
  }

  @Override
  public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
    super.onGuildMemberRoleRemove(event);
  }

  @Override
  public void onGuildMemberUpdate(GuildMemberUpdateEvent event) {
    super.onGuildMemberUpdate(event);
  }

  @Override
  public void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event) {
    super.onGuildMemberUpdateAvatar(event);
  }

  @Override
  public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
    super.onGuildMemberUpdateBoostTime(event);
  }

  @Override
  public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
    super.onGuildMemberUpdateNickname(event);
  }

  @Override
  public void onGuildMemberUpdatePending(GuildMemberUpdatePendingEvent event) {
    super.onGuildMemberUpdatePending(event);
  }

}

package vn.vna.eri.v2.event.discord;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.Event;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.error.ERDiscordGuildPermissionMismatch;
import vn.vna.eri.v2.event.discord.helper.CommandProperties;
import vn.vna.eri.v2.event.discord.helper.CommandType;
import vn.vna.eri.v2.event.discord.helper.ExecutionInfo;
import vn.vna.eri.v2.event.discord.helper.PropertyField;
import vn.vna.eri.v2.services.SVDiscord;

@Getter
@Setter
public abstract class CMDTemplate {
  private static final Logger     logger = LoggerFactory.getLogger(CMDTemplate.class);
  private static Set<CMDTemplate> commandManager;

  @PropertyField
  protected String[]                       commands;
  @PropertyField
  protected String                         description;
  @PropertyField
  protected CommandType                    type;
  @PropertyField
  protected Boolean                        separateThread;
  @PropertyField
  protected Class<? extends CMDTemplate>[] parent;
  @PropertyField
  protected Permission[]                   botPermission;
  @PropertyField
  protected Permission[]                   senderPermission;

  protected Set<CMDTemplate> children;
  protected CMDTemplate      parentCommand;

  protected JDA       jdaContext;
  protected SVDiscord discordService;
  protected Logger    commandLogger;

  public static Set<CMDTemplate> getCommandManager() {
    return CMDTemplate.commandManager;
  }

  public static <T extends CMDTemplate> boolean isRootCommand(T command) {
    return ArrayUtils.isEmpty(command.getParent());
  }

  public static void loadCommands() {
    long beginTime = System.currentTimeMillis();

    Package packageScan = CMDTemplate.class.getPackage();

    logger.info("Command(s) will be loaded from {}", packageScan.getName());
    ConfigurationBuilder reflectionConfigBuilder = new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(CMDTemplate.class.getPackageName()));
    Reflections          reflections             = new Reflections(reflectionConfigBuilder);

    // Collect command classes
    Set<Class<? extends CMDTemplate>> reflectedTypes = reflections
        .getSubTypesOf(CMDTemplate.class).stream()
        .filter(type -> CMDTemplate.class.equals(type.getSuperclass()))
        .collect(Collectors.toSet());

    logger.info("Reflection found {} command class(es)", reflectedTypes.size());

    // Collect injection field
    Set<Field> injectionFields = Arrays.stream(CMDTemplate.class.getDeclaredFields())
        .filter((field -> !Objects.isNull(field.getAnnotation(PropertyField.class))))
        .collect(Collectors.toSet());

    // Create objects
    Set<CMDTemplate> commandCollection = new HashSet<>();
    for (Class<? extends CMDTemplate> reflectedType : reflectedTypes) {
      try {
        Constructor<? extends CMDTemplate> typeDefaultConstructor = reflectedType
            .getConstructor();

        CommandProperties properties = reflectedType.getAnnotation(CommandProperties.class);

        CMDTemplate command = typeDefaultConstructor.newInstance();

        // Inject value
        for (Field injectionField : injectionFields) {
          injectionField.set(command,
              CommandProperties.class.getMethod(injectionField.getName()).invoke(properties));
        }

        commandCollection.add(command);
      } catch (Exception ex) {
        logger.error("Create command object with type [{}] has failed due to error: {}",
            reflectedType.getName(), ex.getMessage());
      }
    }

    // Set some needed field
    for (CMDTemplate command : commandCollection) {
      command.setJdaContext(SVDiscord.getInstance().getJdaContext());
      command.setDiscordService(SVDiscord.getInstance());
      command.setCommandLogger(LoggerFactory.getLogger(command.getClass()));
    }

    // Inspect child commands
    for (CMDTemplate command : commandCollection) {
      command.setChildren(commandCollection.stream()
          .filter(
              (subcommand) -> Arrays.asList(subcommand.getParent()).contains(command.getClass()))
          .collect(Collectors.toSet()));
    }

    // Get all root commands
    CMDTemplate.commandManager = commandCollection.stream()
        .filter(CMDTemplate::isRootCommand).collect(Collectors.toSet());

    logger.info("Scan discord command operation finished took {} ms, found {} root command(s)",
        System.currentTimeMillis() - beginTime, CMDTemplate.commandManager.size());
  }

  private static ExecutionInfo tryToMatchRecursive(String[] commandArray,
      CMDTemplate crrCommand, Integer depth, CMDTemplate root) {
    if (crrCommand.match(commandArray[depth])) {
      if (Objects.isNull(root)) {
        root = crrCommand;
      }

      if (depth < commandArray.length - 1) {
        for (CMDTemplate child : crrCommand.getChildren()) {
          ExecutionInfo matched = tryToMatchRecursive(commandArray, child, depth + 1, root);
          if (Objects.nonNull(matched)) {
            return matched;
          }
        }
      }

      return new ExecutionInfo(crrCommand, depth, root);
    }
    return null;
  }

  public static ExecutionInfo tryToMatch(String[] commandArray) {
    for (CMDTemplate command : getCommandManager()) {
      ExecutionInfo matched = tryToMatchRecursive(commandArray, command, 0, null);
      if (Objects.nonNull(matched)) {
        return matched;
      }
    }
    return null;
  }

  public static boolean tryExecute(String @NotNull [] commandArray, Event event, CommandType type) {
    ExecutionInfo matchedCommand = tryToMatch(commandArray);

    if (Objects.isNull(matchedCommand)) {
      return false;
    }

    if (!matchedCommand.getRootCommand().getType().equals(type)) {
      return false;
    }

    matchedCommand.getCommand().execute(commandArray, event, matchedCommand.getDepth());
    return true;
  }

  private List<Permission> getMismatchPermission(Member member, GuildChannel channel,
      Permission[] requiredPermissions) {
    List<Permission>    mismatch;
    EnumSet<Permission> availablePermissions = member.getPermissions(channel);

    mismatch = Arrays.stream(requiredPermissions)
        .filter((permission) -> !availablePermissions.contains(permission))
        .collect(Collectors.toList());

    return mismatch;
  }

  public void requirePermissionMessageEvent(Member bot, Member sender, GuildChannel channel)
      throws ERDiscordGuildPermissionMismatch {
    List<Permission> botPermissionMisMatch = this.getMismatchPermission(bot, channel,
        this.botPermission);

    if (botPermissionMisMatch.size() > 0) {
      throw new ERDiscordGuildPermissionMismatch(bot, botPermissionMisMatch);
    }

    List<Permission> senderPermissionMismatch = this.getMismatchPermission(sender, channel,
        this.senderPermission);

    if (senderPermissionMismatch.size() > 0) {
      throw new ERDiscordGuildPermissionMismatch(sender, senderPermissionMismatch);
    }
  }

  public Boolean match(String commandStr) {
    return Arrays.asList(this.commands).contains(commandStr);
  }

  public abstract void execute(String[] commandList, Event event, Integer commandDepth);

}

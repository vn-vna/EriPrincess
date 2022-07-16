package vn.vna.eri.v2.event.discord;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
public abstract class CMDDiscordCommand {

  private static final Logger logger = LoggerFactory.getLogger(CMDDiscordCommand.class);
  private static Set<CMDDiscordCommand> commandManager;
  @PropertyField
  protected String[] commands;
  @PropertyField
  protected String description;
  @PropertyField
  protected CommandType type;
  @PropertyField
  protected Boolean separateThread;
  @PropertyField
  protected Class<? extends CMDDiscordCommand>[] parent;
  protected Set<CMDDiscordCommand> children;
  protected CMDDiscordCommand parentCommand;

  public static Set<CMDDiscordCommand> getCommandManager() {
    return CMDDiscordCommand.commandManager;
  }

  public static void loadCommands() {
    Long beginTime = System.currentTimeMillis();

    Package packageScan = CMDDiscordCommand.class.getPackage();

    logger.info("Command(s) will be loaded from {}", packageScan.getName());
    ConfigurationBuilder reflectionConfigBuilder = new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(CMDDiscordCommand.class.getPackageName()));
    Reflections reflections = new Reflections(reflectionConfigBuilder);

    // Collect command classes
    Set<Class<? extends CMDDiscordCommand>> reflectedTypes = reflections
        .getSubTypesOf(CMDDiscordCommand.class)
        .stream()
        .filter(type -> CMDDiscordCommand.class.equals(type.getSuperclass()))
        .collect(Collectors.toSet());

    logger.info("Reflection found {} command class(es)", reflectedTypes.size());

    // Collect injection field
    Set<Field> injectionFields = Arrays
        .stream(CMDDiscordCommand.class.getDeclaredFields())
        .filter((field -> !Objects.isNull(field.getAnnotation(PropertyField.class))))
        .collect(Collectors.toSet());

    // Create objects
    Set<CMDDiscordCommand> commandCollection = new HashSet<>();
    for (Class<? extends CMDDiscordCommand> reflectedType : reflectedTypes) {
      try {
        Constructor<? extends CMDDiscordCommand> typeDefaultConstructor =
            reflectedType.getConstructor();
        CommandProperties properties = reflectedType.getAnnotation(CommandProperties.class);
        CMDDiscordCommand command = typeDefaultConstructor.newInstance();

        // Inject value
        for (Field injectionField : injectionFields) {
          injectionField.set(command,
              CommandProperties.class
                  .getMethod(injectionField.getName())
                  .invoke(properties));
        }

        commandCollection.add(command);
      } catch (Exception ex) {
        logger.error(
            "Create command object with type [{}] has failed due to error: {}",
            reflectedType.getName(),
            ex.getMessage());
      }
    }

    // Inspect child commands
    for (CMDDiscordCommand command : commandCollection) {
      command.setChildren(
          commandCollection
              .stream()
              .filter(
                  (subcommand) -> Arrays
                      .asList(subcommand.getParent())
                      .contains(command.getClass()))
              .collect(Collectors.toSet()));
    }

    // Get all root commands
    CMDDiscordCommand.commandManager = commandCollection
        .stream()
        .filter((command) -> command.getParent().length == 0)
        .collect(Collectors.toSet());

    logger.info("Scan discord command operation finished took {} ms, found {} root command(s)",
        System.currentTimeMillis() - beginTime,
        CMDDiscordCommand.commandManager.size());
  }

  private static ExecutionInfo tryToMatchRecursive(String[] commandArray,
      CMDDiscordCommand crrCommand, Integer depth, CMDDiscordCommand root) {
    if (crrCommand.match(commandArray[depth])) {
      if (Objects.isNull(root)) {
        root = crrCommand;
      }

      if (depth < commandArray.length - 1) {
        for (CMDDiscordCommand child : crrCommand.getChildren()) {
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
    for (CMDDiscordCommand command : getCommandManager()) {
      ExecutionInfo matched = tryToMatchRecursive(commandArray, command, 0, null);
      if (Objects.nonNull(matched)) {
        return matched;
      }
    }
    return null;
  }

  public static void tryExecute(String @NotNull [] commandArray, Event event, CommandType type) {
    ExecutionInfo matchedCommand = tryToMatch(commandArray);

    if (Objects.isNull(matchedCommand)) {
      return;
    }

    if (!matchedCommand.getRootCommand().getType().equals(type)) {
      return;
    }

    matchedCommand.getCommand().preExecute(commandArray, event, matchedCommand.getDepth());
    matchedCommand.getCommand().execute(commandArray, event, matchedCommand.getDepth());
    matchedCommand.getCommand().postExecute(commandArray, event, matchedCommand.getDepth());
  }

  public Boolean match(String commandStr) {
    return Arrays.asList(this.commands).contains(commandStr);
  }

  public void preExecute(String[] commandList, Event event, Integer commandDepth) {
  }

  public abstract void execute(String[] commandList, Event event, Integer commandDepth);

  public void postExecute(String[] commandList, Event event, Integer commandDepth) {
  }

  public enum CommandType {
    MESSAGE_COMMAND,
    SLASH_COMMAND,
    SUBCOMMAND
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface CommandProperties {

    Class<? extends CMDDiscordCommand>[] parent() default {};

    String[] commands();

    String description() default "";

    CommandType type();

    boolean separateThread() default true;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  public @interface PropertyField {

  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class ExecutionInfo {

    private CMDDiscordCommand command;
    private Integer depth;
    private CMDDiscordCommand rootCommand;

  }

}

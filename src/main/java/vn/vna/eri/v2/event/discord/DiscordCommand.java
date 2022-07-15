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
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
public abstract class DiscordCommand {

  private static final Logger logger = LoggerFactory.getLogger(DiscordCommand.class);
  private static Set<DiscordCommand> commandManager;
  @PropertyField
  protected String[] commands;
  @PropertyField
  protected String description;
  @PropertyField
  protected CommandType type;
  @PropertyField
  protected Boolean separateThread;
  @PropertyField
  protected Class<? extends DiscordCommand>[] parent;
  protected Set<DiscordCommand> children;

  public static Set<DiscordCommand> getCommandManager() {
    return DiscordCommand.commandManager;
  }

  public static void loadCommands() {
    Long beginTime = System.currentTimeMillis();

    Package packageScan = DiscordCommand.class.getPackage();

    logger.info("Command(s) will be loaded from {}", packageScan.getName());
    ConfigurationBuilder reflectionConfigBuilder = new ConfigurationBuilder()
        .forPackages(DiscordCommand.class.getPackageName());
    Reflections reflections = new Reflections(reflectionConfigBuilder);

    // Collect command classes
    Set<Class<? extends DiscordCommand>> reflectedTypes = reflections
        .getSubTypesOf(DiscordCommand.class)
        .stream()
        .filter(type -> DiscordCommand.class.equals(type.getSuperclass()))
        .collect(Collectors.toSet());

    logger.info("Reflection found {} command class(es)", reflectedTypes.size());

    // Collect injection field
    Set<Field> injectionFields = Arrays
        .stream(DiscordCommand.class.getDeclaredFields())
        .filter((field -> !Objects.isNull(field.getAnnotation(PropertyField.class))))
        .collect(Collectors.toSet());

    // Create objects
    Set<DiscordCommand> commandCollection = new HashSet<>();
    for (Class<? extends DiscordCommand> reflectedType : reflectedTypes) {
      try {
        Constructor<? extends DiscordCommand> typeDefaultConstructor = reflectedType.getConstructor();
        CommandProperties properties = reflectedType.getAnnotation(CommandProperties.class);
        DiscordCommand command = typeDefaultConstructor.newInstance();

        // Inject value
        for (Field injectionField : injectionFields) {
          injectionField.set(command,
              CommandProperties.class
                  .getMethod(injectionField.getName())
                  .invoke(properties));
        }

        commandCollection.add(command);
      } catch (Exception ex) {
        logger.error("Create command object with type [{}] has failed due to error: {}",
            reflectedType.getName(),
            ex.getMessage());
      }
    }

    // Inspect child commands
    for (DiscordCommand command : commandCollection) {
      command.setChildren(
          commandCollection
              .stream()
              .filter(
                  (subcommand) -> Arrays.asList(subcommand.getParent())
                      .contains(command.getClass()))
              .collect(Collectors.toSet()));
    }

    // Get all root commands
    DiscordCommand.commandManager = commandCollection
        .stream()
        .filter((command) -> command.getParent().length == 0)
        .collect(Collectors.toSet());

    logger.info("Scan discord command operation finished took {} ms, found {} root command(s)",
        System.currentTimeMillis() - beginTime,
        DiscordCommand.commandManager.size());
  }

  private static ExecutionInfo tryToMatchRecursive(String[] commandArray,
      DiscordCommand crrCommand, Integer depth) {
    if (crrCommand.match(commandArray[depth])) {
      if (depth < commandArray.length - 1) {
        for (DiscordCommand child : crrCommand.getChildren()) {
          ExecutionInfo matched = tryToMatchRecursive(commandArray, child, depth + 1);
          if (Objects.nonNull(matched)) {
            return matched;
          }
        }
      }

      return new ExecutionInfo(crrCommand, depth);
    }
    return null;
  }

  public static ExecutionInfo tryToMatch(String[] commandArray) {
    for (DiscordCommand command : getCommandManager()) {
      ExecutionInfo matched = tryToMatchRecursive(commandArray, command, 0);
      if (Objects.nonNull(matched)) {
        return matched;
      }
    }
    return null;
  }

  public static Boolean tryExecute(String @NotNull [] commandArray, Event event) {
    ExecutionInfo matchedCommand = tryToMatch(commandArray);
    if (Objects.nonNull(matchedCommand)) {
      matchedCommand.getCommand().preExecute(commandArray, event, matchedCommand.getDepth());
      matchedCommand.getCommand().execute(commandArray, event, matchedCommand.getDepth());
      matchedCommand.getCommand().postExecute(commandArray, event, matchedCommand.getDepth());
      return true;
    }
    return false;
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

    Class<? extends DiscordCommand>[] parent() default {};

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

    private DiscordCommand command;
    private Integer depth;

  }

}

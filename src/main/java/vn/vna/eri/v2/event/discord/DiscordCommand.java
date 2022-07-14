package vn.vna.eri.v2.event.discord;

public abstract class MessageCommand {

  protected String command;
  protected String description;

  public abstract Boolean match(String str);

  public abstract void execute();

  public enum CommandType {
    MESSAGE_COMMAND, SLASH_COMMAND
  }

  public @interface Command {
    String command();

    CommandType type();
  }
}

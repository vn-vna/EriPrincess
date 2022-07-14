package vn.vna.eri.v2.error;

public class DiscordServiceExists extends IllegalStateException {

  public DiscordServiceExists() {
    super("Discord bot service has already been initialized");
  }
}

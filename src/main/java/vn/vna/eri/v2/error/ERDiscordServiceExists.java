package vn.vna.eri.v2.error;

public class ERDiscordServiceExists extends IllegalStateException {

  public ERDiscordServiceExists() {
    super("Discord bot service has already been initialized");
  }
}

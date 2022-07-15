package vn.vna.eri.v2.configs;

public class Env extends ConfigManager {

  public static final String ENV_DATASOURCE = "DATASOURCE";
  public static final String ENV_DBUSER = "DBUSER";
  public static final String ENV_DBPWD = "DBPWD";
  public static final String ENV_BOT_TOKEN = "BOT_TOKEN";
  public static final String ENV_DISABLE_DISCORD = "DISABLE_DISCORD";
  public static final String ENV_DISABLE_API = "DISABLE_API";
  public static final String ENV_BOT_PREFIX = "BOT_PREFIX";

  @Override
  public String getString(String key) {
    return System.getenv(key);
  }

}

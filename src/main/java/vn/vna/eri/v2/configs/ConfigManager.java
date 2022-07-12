package vn.vna.eri.v2.configs;

public abstract class ConfigManager {

  static {
    envManager = new Env();
  }

  public abstract String getString(String key);

  public Boolean getBoolean(String key) {
    try {
      return Boolean.parseBoolean(this.getString(key));
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  public Integer getInteger(String key) {
    try {
      return Integer.parseInt(this.getString(key));
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  public Long getLong(String key) {
    try {
      return Long.parseLong(this.getString(key));
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  public Float getFloat(String key) {
    try {
      return Float.parseFloat(this.getString(key));
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  public Double getDouble(String key) {
    try {
      return Double.parseDouble(this.getString(key));
    } catch (NumberFormatException nfex) {
      return null;
    }
  }

  public static Env getEnvManager() {
    return ConfigManager.envManager;
  }

  public static Env envManager;
}

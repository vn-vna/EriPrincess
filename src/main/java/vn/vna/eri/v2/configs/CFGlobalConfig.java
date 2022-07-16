package vn.vna.eri.v2.configs;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.clients.CLServerConfig;

public class CFGlobalConfig {


  public static final String ENV_DATASOURCE = "DATASOURCE";
  public static final String ENV_DBUSER = "DBUSER";
  public static final String ENV_DBPWD = "DBPWD";
  public static final String ENV_BOT_TOKEN = "BOT_TOKEN";
  public static final String ENV_DISABLE_DISCORD = "DISABLE_DISCORD";
  public static final String ENV_DISABLE_API = "DISABLE_API";
  public static final String ENV_BOT_PREFIX = "BOT_PREFIX";

  public static Logger logger;
  private static CFGlobalConfig instance;

  static {
    logger = LoggerFactory.getLogger(CFGlobalConfig.class);
  }

  public static CFGlobalConfig getInstance() {
    synchronized (CFGlobalConfig.class) {
      if (Objects.isNull(CFGlobalConfig.instance)) {
        instance = new CFGlobalConfig();
      }
    }
    return CFGlobalConfig.instance;
  }


  public String getString(String key) {
    String value;
    try {
      value = CLServerConfig.getClient().getString(key);
    } catch (Exception ex) {
      logger.warn(
          "Get configuration variable from database failed due to error: {}. Trying to get from ENV instead",
          ex.getMessage());
    }
    // Get value from ENV variables
    value = System.getenv(key);
    return value;
  }

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
}

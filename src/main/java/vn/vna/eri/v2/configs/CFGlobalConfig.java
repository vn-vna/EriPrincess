package vn.vna.eri.v2.configs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.clients.CLServerConfig;
import vn.vna.eri.v2.configs.helper.ConfigTarget;
import vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage;
import vn.vna.eri.v2.configs.helper.LoadConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.utils.UTSingleton;

public class CFGlobalConfig {

  public static final String ENV_DATASOURCE = "DATASOURCE";
  public static final String ENV_DBUSER = "DBUSER";
  public static final String ENV_DBPWD = "DBPWD";
  public static final String ENV_BOT_TOKEN = "BOT_TOKEN";
  public static final String ENV_DISABLE_DISCORD = "DISABLE_DISCORD";
  public static final String ENV_DISABLE_API = "DISABLE_API";
  public static final String ENV_BOT_PREFIX = "BOT_PREFIX";

  public static final String CFG_BOT_NAME = "BOT_NAME";
  public static final String CFG_BOT_EMBED_TITLE = "BOT_EMBED_TITLE";
  public static final String CFG_BOT_EMBED_TITLE_URL = "BOT_EMBED_TITLE_URL";
  public static final String CFG_BOT_EMBED_THUMB_URL = "BOT_EMBED_THUMB_URL";
  public static final String CFG_BOT_EMBED_FOOTER = "BOT_EMBED_FOOTER";

  private static Logger logger;
  private static CFGlobalConfig instance;

  private Set<Class<? extends UpdatableConfigTarget>> configTargets;

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

  public CFGlobalConfig() {
    this.scanConfigTargets();
  }

  public String getString(String key) {
    try {
      if (Objects.nonNull(SVApiControl.getInstance()) &&
          Objects.nonNull(SVApiControl.getApplicationContext())) {
        String result = CLServerConfig
            .getClient()
            .getString(key);
        if (Objects.nonNull(result)) {
          return result;
        }
      }
    } catch (Exception ex) {
      logger.warn(
          "Get configuration variable from database failed due to error: {}. Trying to get from ENV instead",
          ex.getMessage());
    }
    // Get value from ENV variables
    return System.getenv(key);
  }

  public String getString(String key, String fallback) {
    String result = this.getString(key);
    if (Objects.nonNull(result)) {
      return result;
    }
    return fallback;
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

  public void scanConfigTargets() {
    logger.info("Triggered scan config target");

    ConfigurationBuilder reflectionConfigBuilder = new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(CFGlobalConfig.class.getPackageName()));
    Reflections reflections = new Reflections(reflectionConfigBuilder);

    this.configTargets = reflections
        .getSubTypesOf(UpdatableConfigTarget.class)
        .stream()
        .filter((type) -> Objects.nonNull(type.getAnnotation(ConfigTarget.class)))
        .collect(Collectors.toSet());

    logger.info("Scanned {} config target(s)", this.configTargets.size());

    this.configTargets
        .forEach((type) -> {
          UTSingleton.getInstanceOf(type);
        });
  }

  public void invokeUpdateAtStage(ConfigTargetLoadStage stage) {
    logger.info("Invoking configuration loader from stage {}", stage.getStageName());
    this.configTargets
        .forEach((type) -> {
          ConfigTarget property = type.getAnnotation(ConfigTarget.class);
          if (property.value().equals(stage)) {
            logger.info("Loading configuration class [{}]", type.getSimpleName());
            UTSingleton
                .getInstanceOf(type)
                .ifPresent((u) -> u.update());
          }
        });
  }

  public void loadConfigForObject(Object obj) {
    List<Field> configFields = Arrays
        .stream(obj.getClass().getDeclaredFields())
        .filter((field) -> Objects.nonNull(field.getAnnotation(LoadConfig.class)))
        .toList();

    for (Field field : configFields) {
      try {
        field.setAccessible(true);
        LoadConfig annotated = field.getAnnotation(LoadConfig.class);
        Object value = null;
        {
          if (annotated.type().equals(String.class)) {
            value = this.getString(annotated.value());
          } else if (annotated.type().equals(Boolean.class)) {
            value = this.getBoolean(annotated.value());
          } else if (annotated.type().equals(Integer.class)) {
            value = this.getInteger(annotated.value());
          } else if (annotated.type().equals(Long.class)) {
            value = this.getLong(annotated.value());
          } else if (annotated.type().equals(Float.class)) {
            value = this.getFloat(annotated.value());
          } else if (annotated.type().equals(Double.class)) {
            value = this.getDouble(annotated.value());
          }
        }
        field.set(obj, value);
        logger.info(
            "Loaded config value for field {} with alias {}",
            field.getName(),
            annotated.value());
      } catch (Exception ex) {
        CFGlobalConfig.logger.error(
            "Can't inject value for field {} due to error: {}",
            field.getName(),
            ex.getMessage());
      } finally {
        field.setAccessible(false);
      }
    }
  }
}

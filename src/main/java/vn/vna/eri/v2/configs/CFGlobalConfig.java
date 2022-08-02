package vn.vna.eri.v2.configs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
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

  public static final String CT_NAME_DSC_EVLISTENER    = "bot-event-listener";
  public static final String CT_NAME_DSC_MSG_BUILDER   = "bot-msg-builder";
  public static final String CT_NAME_SPRING_DATASOURCE = "spring-data-source";

  public static final String ENV_DATASOURCE      = "DATASOURCE";
  public static final String ENV_DBUSER          = "DBUSER";
  public static final String ENV_DBPWD           = "DBPWD";
  public static final String CFG_BOT_TOKEN       = "BOT_TOKEN";
  public static final String ENV_DISABLE_DISCORD = "DISABLE_DISCORD";
  public static final String ENV_DISABLE_API     = "DISABLE_API";

  public static final String CFG_BOT_PREFIX          = "BOT_PREFIX";
  public static final String CFG_BOT_NAME            = "BOT_NAME";
  public static final String CFG_BOT_EMBED_TITLE     = "BOT_EMBED_TITLE";
  public static final String CFG_BOT_EMBED_TITLE_URL = "BOT_EMBED_TITLE_URL";
  public static final String CFG_BOT_EMBED_THUMB_URL = "BOT_EMBED_THUMB_URL";
  public static final String CFG_BOT_EMBED_FOOTER    = "BOT_EMBED_FOOTER";

  private static final Logger   logger = LoggerFactory.getLogger(CFGlobalConfig.class);
  private static CFGlobalConfig instance;

  @Getter
  private Map<String, Class<? extends UpdatableConfigTarget>> configTargets;

  public CFGlobalConfig() {
    this.scanConfigTargets();
  }

  public static CFGlobalConfig getInstance() {
    synchronized (CFGlobalConfig.class) {
      if (Objects.isNull(CFGlobalConfig.instance)) {
        instance = new CFGlobalConfig();
      }
    }
    return CFGlobalConfig.instance;
  }

  public String requestConfigValue(String key) {
    try {
      if (Objects.nonNull(SVApiControl.getInstance())
        && Objects.nonNull(SVApiControl.getApplicationContext())) {
        String result = CLServerConfig.getClient().getString(key);
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

  public Optional<String> getString(String key) {
    return Optional.ofNullable(this.requestConfigValue(key));
  }

  public Optional<Boolean> getBoolean(String key) {
    return this.getString(key).map(Boolean::parseBoolean);
  }

  public Optional<Integer> getInteger(String key) {
    return this.getString(key).map(Integer::parseInt);
  }

  public Optional<Long> getLong(String key) {
    return this.getString(key).map(Long::parseLong);
  }

  public Optional<Float> getFloat(String key) {
    return this.getString(key).map(Float::parseFloat);
  }

  public Optional<Double> getDouble(String key) {
    return this.getString(key).map(Double::parseDouble);
  }

  public void scanConfigTargets() {
    logger.info("Triggered scan config target");
    this.configTargets = new HashMap<>();

    ConfigurationBuilder reflectionConfigBuilder = new ConfigurationBuilder()
      .setUrls(ClasspathHelper.forPackage(CFGlobalConfig.class.getPackageName()));
    Reflections          reflections             = new Reflections(reflectionConfigBuilder);

    reflections.getSubTypesOf(UpdatableConfigTarget.class).stream()
      .filter((type) -> Objects.nonNull(type.getAnnotation(ConfigTarget.class)))
      .forEach((type) -> {
        ConfigTarget properties = type.getAnnotation(ConfigTarget.class);
        this.configTargets.put(properties.name(), type);
      });

    logger.info("Scanned {} config target(s)", this.configTargets.size());

    // Invoke getInstance once to construct all of them
    this.configTargets.forEach((key, type) -> UTSingleton.getInstanceOf(type));
  }

  public void invokeUpdateAtStage(ConfigTargetLoadStage stage) {
    logger.info("Invoking configuration loader from stage {}", stage.getStageName());
    this.configTargets.forEach((key, type) -> {
      ConfigTarget property = type.getAnnotation(ConfigTarget.class);
      if (property.stage().equals(stage)) {
        logger.info("Loading configuration class [{}]", type.getSimpleName());
        UTSingleton.getInstanceOf(type).ifPresent(UpdatableConfigTarget::update);
      }
    });
  }

  public void loadConfigForObject(Object obj) {
    List<Field> configFields = Arrays.stream(obj.getClass().getDeclaredFields())
      .filter((field) -> Objects.nonNull(field.getAnnotation(LoadConfig.class))).toList();

    for (Field field : configFields) {
      try {
        field.setAccessible(true);
        LoadConfig  annotated = field.getAnnotation(LoadConfig.class);
        Optional<?> value     = null;
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
        field.set(obj, value.orElse(null));
        logger.info("Loaded config value for field {} with alias {}", field.getName(),
          annotated.value());
      } catch (Exception ex) {
        CFGlobalConfig.logger.error("Can't inject value for field {} due to error: {}",
          field.getName(), ex.getMessage());
      } finally {
        field.setAccessible(false);
      }
    }
  }
}

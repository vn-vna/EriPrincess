package vn.vna.eri.v2.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.utils.helper.BoolConfigProperty;

public class UTBoolConfigString {

  protected Logger      logger;
  protected String      separator;
  protected List<Field> configFields;

  public UTBoolConfigString() {
    this.logger    = LoggerFactory.getLogger(this.getClass());
    this.separator = ",";
    initializeObject();
  }

  private void initializeObject() {
    this.configFields = gatherConfigField();
    this.clearConfig();
  }

  private void clearConfig() {
    for (Field field : this.configFields) {
      field.setAccessible(true);
      try {
        field.set(this, false);
      } catch (Exception ex) {
        logger.error("Clear config error due to error: {}", ex.getMessage());
      }
      field.setAccessible(false);
    }
  }

  public void importConfigString(String str) {
    this.clearConfig();

    List<String> tokens = Arrays.stream(str.split(this.separator))
        .filter(token -> !"".equals(token)).toList();

    for (Field configField : this.configFields) {
      try {
        configField.setAccessible(true);
        BoolConfigProperty property = configField.getAnnotation(BoolConfigProperty.class);
        if (tokens.contains(configField.getName()) || tokens.contains(property.alias())) {
          configField.set(this, true);
        } else {
          configField.set(this, false);
        }
      } catch (Exception ex) {
        this.logger.error(
            "Can't inject value of field {} while parsing config string due to error: {}",
            configField.getName(), ex.getMessage());
      } finally {
        configField.setAccessible(false);
      }
    }
  }

  private List<Field> gatherConfigField() {
    // Gather fields
    return Arrays.stream(this.getClass().getDeclaredFields())
        .filter(field -> Objects.nonNull(field.getAnnotation(BoolConfigProperty.class))
            && field.getType().equals(Boolean.class))
        .sorted(Comparator.comparing(Field::getName)).toList();
  }

  public String toConfigString() {

    StringBuilder builder = new StringBuilder();

    for (Field configField : this.configFields) {
      try {
        configField.setAccessible(true);

        if (configField.get(this) instanceof Boolean value && value) {
          // Get config name if field has alias
          String             configName;
          BoolConfigProperty property = configField.getAnnotation(BoolConfigProperty.class);
          if ("".equals(property.alias())) {
            configName = configField.getName();
          } else {
            configName = property.alias();
          }

          // Append to string
          builder.append(configName).append(this.separator);
        }
      } catch (Exception ex) {
        this.logger.error("Unable to parse config field [{}] due to error: {}",
            configField.getName(), ex.getMessage());
      } finally {
        configField.setAccessible(false);
      }
    }

    // Try to remove last separator
    if (builder.isEmpty()) {
      return builder.toString();
    }

    builder.deleteCharAt(builder.length() - 1);
    return builder.toString();
  }

  @Override
  public String toString() {
    return toConfigString();
  }
}

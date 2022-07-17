package vn.vna.eri.v2.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.eri.v2.utils.annotation.BoolConfigProperty;

@Getter
@Setter
public class UTCombinedBoolConfigString {


  protected Character combinedSeparator;
  protected Character pairSeparator;
  protected Logger logger;

  public UTCombinedBoolConfigString() {
    this.combinedSeparator = ' ';
    this.pairSeparator = ':';
    logger = LoggerFactory.getLogger(this.getClass());
  }

  private List<Field> gatherConfigField() {
    return Arrays
        .stream(this.getClass().getDeclaredFields())
        .filter((field) ->
            UTBoolConfigString.class.isAssignableFrom(field.getType()) &&
                Objects.nonNull(field.getAnnotation(BoolConfigProperty.class)))
        .sorted(Comparator.comparing(Field::getName))
        .toList();
  }

  public void importFromConfigString(String str) {
    String[] tokens = str.split(String.valueOf(this.combinedSeparator));

    for (String token : tokens) {
      String[] configPair = token.split(String.valueOf(this.pairSeparator));
      if (configPair.length != 2) {
        this.logger.warn(
            "Parse config string [{}] failed due to error: Token is invalid",
            token);
        continue;
      }

      String configName = configPair[0];
      String configString = configPair[1];

      try {
        Field configField = this.getClass().getDeclaredField(configName);
        if (UTBoolConfigString.class.isAssignableFrom(configField.getType())) {
          configField.setAccessible(true);
          UTBoolConfigString configObj =
              (UTBoolConfigString) configField.getType().getConstructor().newInstance();
          configObj.importConfigString(configString);

          configField.set(this, configObj);
          configField.setAccessible(false);
        }
        configField.setAccessible(true);
      } catch (Exception ex) {
        logger.error(
            "Can't inject value of field [{}] due to error: {}",
            configName,
            ex.getMessage());
      }
    }
  }

  public String toCombinedString() {
    StringBuilder builder = new StringBuilder();
    // Gather config field
    List<Field> configFields = this.gatherConfigField();

    for (Field configField : configFields) {
      try {
        configField.setAccessible(true);
        BoolConfigProperty fieldProperty = configField.getAnnotation(BoolConfigProperty.class);
        if (configField.get(this) instanceof UTBoolConfigString boolConfigString) {
          String configName;
          if ("".equals(fieldProperty.alias())) {
            configName = configField.getName();
          } else {
            configName = fieldProperty.alias();
          }

          String str = boolConfigString.toConfigString();

          if ("".equals(str)) {
            continue;
          }

          builder
              .append(configName)
              .append(this.pairSeparator)
              .append(str)
              .append(this.combinedSeparator);
        }
      } catch (Exception ex) {
        this.logger.error(
            "Collect config field failed [{}] due to error: {}",
            configField.getName(),
            ex.getMessage());
      } finally {
        configField.setAccessible(false);
      }
    }

    if (builder.isEmpty()) {
      return builder.toString();
    }

    builder.deleteCharAt(builder.length() - 1);
    return builder.toString();
  }

  @Override
  public String toString() {
    return this.toCombinedString();
  }
}

package vn.vna.eri.v2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConvertableToDataObject<DataClass extends JsonClass> {
  private static final String ERROR_STRING = "Convert [{}] to data object [{}] has failed. Due to error {}";
  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(ConvertableToDataObject.class);
  }

  protected Class<DataClass> type;

  public ConvertableToDataObject(Class<DataClass> type) {
    this.type = type;
  }

  public DataClass toDataObject() {
    DataClass obj = null;
    try {
      obj = type.getConstructor().newInstance();

      var setMethods = Arrays.asList(type.getMethods())
          .stream()
          .filter((method) -> {
            return method.getName().startsWith("set");
          })
          .collect(Collectors.toList());

      for (var setMethod : setMethods) {
        try {
          String getMethodName = "get" + setMethod.getName().substring(3);
          Object ret = this.getClass()
              .getMethod(getMethodName)
              .invoke(this);
          setMethod.invoke(obj, ret);
        } catch (Exception ex) {
          logger.error(ERROR_STRING, type.getName(), this.getClass().getName(), ex.getMessage());
        }
      }

    } catch (Exception ex) {
      logger.error(ERROR_STRING, type.getName(), this.getClass().getName(), ex.getMessage());
    }
    return obj;
  }
}

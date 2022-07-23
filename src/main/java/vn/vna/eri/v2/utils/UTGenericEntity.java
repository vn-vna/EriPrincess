package vn.vna.eri.v2.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UTGenericEntity<DataClass extends UTJsonClass> {

  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(UTGenericEntity.class);
  }

  protected Class<DataClass> type;

  public UTGenericEntity(Class<DataClass> type) {
    this.type = type;
  }

  public DataClass toDataObject() {
    DataClass obj = null;
    try {
      obj = type.getConstructor().newInstance();

      var setMethods = Arrays.stream(type.getMethods())
          .filter((method) -> method.getName().startsWith("set"))
          .toList();

      for (var setMethod : setMethods) {
        String getMethodName = "get" + setMethod.getName().substring(3);
        Object ret = this.getClass()
            .getMethod(getMethodName)
            .invoke(this);
        setMethod.invoke(obj, ret);
      }

    } catch (Exception ex) {
      logger.error("Convert [{}] to data object [{}] has failed due to error {}",
          this.getClass().getName(), type.getName(), ex.getMessage());
    }
    return obj;
  }

  public void importFromDataObject(DataClass object, Boolean excludeNull) {
    try {
      List<Method> setMethods = Arrays.stream(this.getClass().getMethods())
          .filter((method) -> method.getName().startsWith("set"))
          .toList();

      for (Method setMethod : setMethods) {
        String getMethodName =
            "get" + setMethod.getName().substring(3); // setSomething -> getSomething

        Object ret = object.getClass()
            .getMethod(getMethodName)
            .invoke(object);

        if (!excludeNull && Objects.isNull(ret)) {
          continue;
        }

        setMethod.invoke(this, ret);
      }
    } catch (Exception ex) {
      logger.error("Import data from data object [{}] into [{}] has failed due to error: {}",
          object.getClass().getName(),
          this.getClass().getName(),
          ex.getMessage());
    }
  }

  public void importFromDataObject(DataClass object) {
    this.importFromDataObject(object, false);
  }
}

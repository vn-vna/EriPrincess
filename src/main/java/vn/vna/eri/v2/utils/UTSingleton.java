package vn.vna.eri.v2.utils;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UTSingleton {

  private static final String METHODNAME_GET_INSTANCE = "getInstance";
  private static final Logger logger = LoggerFactory.getLogger(UTSingleton.class);

  public static <T extends Object> Optional<T> getInstanceOf(@Nonnull Class<T> singletonClass) {
    try {
      return Optional.ofNullable(
          (T) singletonClass.getMethod(METHODNAME_GET_INSTANCE).invoke(null));
    } catch (Exception ex) {
      logger.error(
          "Can't get instance from class {} due to error {}",
          singletonClass.getName(),
          ex.getMessage());
    }
    return Optional.empty();
  }
}

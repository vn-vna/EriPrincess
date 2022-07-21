package vn.vna.eri.v2.configs.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the config value must be loaded from configuration manager
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LoadConfig {

  String value();

  Class<?> type() default String.class;
}

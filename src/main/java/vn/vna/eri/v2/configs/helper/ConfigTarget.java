package vn.vna.eri.v2.configs.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The configuration will be loaded while system loading and can be reload every time. Configuration
 * objects annotated with this annotation must be singleton implemented.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigTarget {

  ConfigTargetLoadStage stage();

  String name();
}

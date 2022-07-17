package vn.vna.eri.v2.event.discord;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandProperties {

  Class<? extends DiscordCommand>[] parent() default {};

  String[] commands();

  String description() default "";

  CommandType type();

  boolean separateThread() default true;
}
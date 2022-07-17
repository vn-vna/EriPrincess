package vn.vna.eri.v2.event.discord.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import vn.vna.eri.v2.event.discord.CMDDiscordCommand;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandProperties {

  Class<? extends CMDDiscordCommand>[] parent() default {};

  String[] commands();

  String description() default "";

  CommandType type();

  boolean separateThread() default true;
}
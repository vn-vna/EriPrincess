package vn.vna.eri.v2.event.discord.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.dv8tion.jda.api.Permission;
import vn.vna.eri.v2.event.discord.CMDTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandProperties {

  Class<? extends CMDTemplate>[] parent() default {};

  String[] commands();

  String descriptionKey() default "";

  CommandType type();

  boolean separateThread() default true;

  Permission[] botPermission() default {};

  Permission[] senderPermission() default {};
}

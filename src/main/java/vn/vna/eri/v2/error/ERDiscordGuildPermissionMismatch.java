package vn.vna.eri.v2.error;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;

@Getter
@Setter
public class ERDiscordGuildPermissionMismatch extends IllegalAccessException {

  protected List<Permission> mismatchPermission;

  public ERDiscordGuildPermissionMismatch(List<Permission> mismatch) {
    super("DiscordGuildPermissionMismatch");
    this.mismatchPermission = mismatch;
  }
}

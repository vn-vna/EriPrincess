package vn.vna.eri.v2.error;

import java.util.List;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

@Getter
public class ERDiscordGuildPermissionMismatch extends IllegalAccessException {

  protected List<Permission> mismatchPermission;
  protected Member member;

  public ERDiscordGuildPermissionMismatch(Member member, List<Permission> mismatch) {
    super("Permission list mismatch");
    this.mismatchPermission = mismatch;
    this.member = member;
  }
}

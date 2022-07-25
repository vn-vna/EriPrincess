package vn.vna.eri.v2.schema;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.vna.eri.v2.utils.UTJsonClass;

@Data
@NoArgsConstructor
public class DCAuthorizeToken
    implements UTJsonClass {

  private String token;
  private String permissions;

  public CSPermissions getPermissionObj() {
    return CSPermissions.fromString(this.getPermissions());
  }

}

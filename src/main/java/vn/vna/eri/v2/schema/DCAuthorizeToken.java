package vn.vna.eri.v2.schema;

import lombok.Data;
import vn.vna.eri.v2.utils.UTJsonClass;

@Data
public class DCAuthorizeToken implements UTJsonClass {

  private String token;
  private String permissions;

  public CSPermissions getPermissionObj() {
    return CSPermissions.fromString(this.getPermissions());
  }

}

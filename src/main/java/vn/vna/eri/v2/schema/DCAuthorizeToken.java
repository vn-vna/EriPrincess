package vn.vna.eri.v2.schema;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vna.eri.v2.utils.UTJsonClass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DCAuthorizeToken implements UTJsonClass {

  private String token;
  private String permissions;

  public CSPermissions getPermissionObj() {
    return CSPermissions.fromString(this.getPermissions());
  }

}

package vn.vna.eri.v2.schema;

import lombok.Data;
import vn.vna.eri.v2.utils.UTCombinedBoolConfigString;
import vn.vna.eri.v2.utils.helper.BoolConfigProperty;

@Data
public class CSPermissions extends UTCombinedBoolConfigString {

  @BoolConfigProperty
  private CSPermissionStatus api;
  @BoolConfigProperty
  private CSPermissionStatus discord;
  @BoolConfigProperty
  private CSPermissionStatus status;

  public CSPermissions() {
    api = new CSPermissionStatus();
    discord = new CSPermissionStatus();
  }

  public static CSPermissions fromString(String str) {
    CSPermissions result = new CSPermissions();
    result.importFromConfigString(str);
    return result;
  }

}

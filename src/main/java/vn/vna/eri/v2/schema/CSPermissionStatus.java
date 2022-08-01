package vn.vna.eri.v2.schema;

import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.utils.UTBoolConfigString;
import vn.vna.eri.v2.utils.helper.BoolConfigProperty;

@Getter
@Setter
public class CSPermissionStatus
    extends UTBoolConfigString {

  @BoolConfigProperty
  protected Boolean read;
  @BoolConfigProperty
  protected Boolean write;

  public CSPermissionStatus() {
    super();
  }

}

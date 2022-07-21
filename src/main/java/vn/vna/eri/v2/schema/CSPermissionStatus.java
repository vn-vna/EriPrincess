package vn.vna.eri.v2.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.utils.helper.BoolConfigProperty;
import vn.vna.eri.v2.utils.UTBoolConfigString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CSPermissionStatus extends UTBoolConfigString {

  @BoolConfigProperty
  protected Boolean read;
  @BoolConfigProperty
  protected Boolean write;

  public CSPermissionStatus() {
    super();
  }

}

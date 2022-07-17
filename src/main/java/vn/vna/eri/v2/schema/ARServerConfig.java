package vn.vna.eri.v2.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vna.eri.v2.utils.UTJsonClass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ARServerConfig extends UTJsonClass {

  private Boolean success;
  private String error;
  private Long took;
  private DCServerConfigInfo result;

}

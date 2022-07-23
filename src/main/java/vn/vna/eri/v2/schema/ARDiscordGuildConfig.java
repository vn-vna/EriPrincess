package vn.vna.eri.v2.schema;

import lombok.Data;
import vn.vna.eri.v2.utils.UTJsonClass;

@Data
public final class ARDiscordGuildConfig implements UTJsonClass {

  private Boolean success;
  private String error;
  private Long took;
  private DCGuildConfig result;

}

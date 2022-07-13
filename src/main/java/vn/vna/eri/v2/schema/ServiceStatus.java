package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import vn.vna.eri.v2.utils.JsonClass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceStatus extends JsonClass {
  private String status;
  @SerializedName("last_startup")
  private String lastStartUp;
}

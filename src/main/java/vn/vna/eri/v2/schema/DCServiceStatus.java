package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
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
public class DCServiceStatus implements UTJsonClass {

  public static final String STATUS_ONLINE = "online";
  public static final String STATUS_OFFLINE = "offline";
  public static final String STATUS_ERROR = "error";
  public static final String STATUS_SUSPEND = "suspend";

  private String status;
  @SerializedName("last_startup")
  private String lastStartUp;
}

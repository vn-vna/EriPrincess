package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import java.lang.management.ManagementFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.vna.eri.v2.utils.UTJsonClass;

@Data
@NoArgsConstructor
public class DCServerStatus
    implements UTJsonClass {

  @SerializedName("system_status")
  DCSystemStatus systemStatus;
  @SerializedName("up_time")
  Long           upTime;
  @SerializedName("discord_service")
  String         discordService;
  @SerializedName("api_service")
  String         apiService;

  public static DCServerStatus getStatus() {
    DCServerStatus result = new DCServerStatus();
    result.systemStatus = DCSystemStatus.getCurrentStatus();
    result.upTime       = ManagementFactory.getRuntimeMXBean().getUptime();
    return result;
  }
}

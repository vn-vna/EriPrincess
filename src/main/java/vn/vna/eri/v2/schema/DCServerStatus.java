package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import java.lang.management.ManagementFactory;
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
@EqualsAndHashCode(callSuper = true)
public class DCServerStatus extends UTJsonClass {

  @SerializedName("system_status")
  DCSystemStatus systemStatus;
  @SerializedName("up_time")
  Long upTime;
  @SerializedName("discord_service")
  String discordService;
  @SerializedName("api_service")
  String apiService;

  public static DCServerStatus getStatus() {
    DCServerStatus result = new DCServerStatus();
    result.systemStatus = DCSystemStatus.getCurrentStatus();
    result.upTime = ManagementFactory.getRuntimeMXBean().getUptime();
    return result;
  }
}

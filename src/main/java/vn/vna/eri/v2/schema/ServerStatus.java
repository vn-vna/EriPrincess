package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import vn.vna.eri.v2.utils.JsonClass;

import java.lang.management.ManagementFactory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerStatus extends JsonClass {
  @SerializedName("system_status")
  SystemStatus systemStatus;
  @SerializedName("up_time")
  Long upTime;
  @SerializedName("discord_service")
  String discordService;
  @SerializedName("api_service")
  String apiService;

  public static ServerStatus getStatus() {
    ServerStatus result = new ServerStatus();
    result.systemStatus = SystemStatus.getCurrentStatus();
    result.upTime = ManagementFactory.getRuntimeMXBean().getUptime();
    return result;
  }
}

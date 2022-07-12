package vn.vna.eri.v2.schema;

import java.lang.management.ManagementFactory;

import com.google.gson.annotations.SerializedName;

import vn.vna.eri.v2.utils.JsonClass;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerStatus extends JsonClass {
  public static ServerStatus getStatus() {
    ServerStatus result = new ServerStatus();
    result.systemStatus = SystemStatus.getCurrentStatus();
    result.upTime = ManagementFactory.getRuntimeMXBean().getUptime();
    return result;
  }

  @SerializedName("system_status")
  SystemStatus systemStatus;
  @SerializedName("up_time")
  Long upTime;
  @SerializedName("discord_service")
  String discordService;
  @SerializedName("api_service")
  String apiService;
}

package vn.vna.eri.v2.schema;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import oshi.SystemInfo;
import vn.vna.eri.v2.utils.UTJsonClass;

@Data
@NoArgsConstructor
public class DCSystemStatus implements UTJsonClass {

  @SerializedName("cpu_count")
  private Integer cpuCount;
  @SerializedName("cpu_usage")
  private Double avgCpuUsage;
  @SerializedName("max_heap")
  private Long maxHeapSize;
  @SerializedName("crr_heap")
  private Long crrHeapSize;
  @SerializedName("mem_total")
  private Long memoryTotal;
  @SerializedName("mem_free")
  private Long memoryFree;
  @SerializedName("os_version")
  private String osVersion;
  @SerializedName("os_name")
  private String osName;
  @SerializedName("os_manufacture")
  private String osManufacture;

  public static DCSystemStatus getCurrentStatus() {
    DCSystemStatus result = new DCSystemStatus();
    SystemInfo sysInfo = new SystemInfo();

    result.avgCpuUsage = sysInfo.getHardware().getProcessor().getSystemCpuLoad(30);
    result.cpuCount = sysInfo.getHardware().getProcessor().getLogicalProcessorCount();
    result.crrHeapSize = Runtime.getRuntime().totalMemory();
    result.maxHeapSize = Runtime.getRuntime().maxMemory();
    result.memoryTotal = sysInfo.getHardware().getMemory().getTotal();
    result.memoryFree = sysInfo.getHardware().getMemory().getAvailable();
    result.osName = sysInfo.getOperatingSystem().getFamily();
    result.osVersion = sysInfo.getOperatingSystem().getVersionInfo().getVersion();
    result.osManufacture = sysInfo.getOperatingSystem().getManufacturer();

    return result;
  }

}

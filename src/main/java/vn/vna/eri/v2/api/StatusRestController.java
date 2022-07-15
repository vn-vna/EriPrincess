package vn.vna.eri.v2.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.schema.ServerStatus;
import vn.vna.eri.v2.services.ApiService;
import vn.vna.eri.v2.services.DiscordService;
import vn.vna.eri.v2.utils.ApiResponse;

@RestController
public class StatusRestController {

  @GetMapping("/api/status/server")
  public ResponseEntity<String> getServerStatus() {
    return ApiResponse.responseJson(ApiResponse.STATUS_OK, ServerStatus.getStatus());
  }

  @GetMapping("/api/status/service/api")
  public ResponseEntity<String> getApiServiceStatus() {
    return ApiResponse.responseJson(ApiResponse.STATUS_OK, ApiService.getInstance().getStatus());
  }

  @GetMapping("/api/status/service/discord")
  public ResponseEntity<String> getDiscordServiceStatus() {
    return ApiResponse.responseJson(ApiResponse.STATUS_OK,
        DiscordService.getInstance().getStatus());
  }
}

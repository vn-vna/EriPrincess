package vn.vna.eri.v2.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.schema.ServerStatus;
import vn.vna.eri.v2.utils.ApiResponse;

@RestController
public class StatusRestController {

  @GetMapping("/api/server/status")
  public ResponseEntity<String> getServerStatus() {
    return ApiResponse.responseJson(ApiResponse.STATUS_OK, ServerStatus.getStatus());
  }

}

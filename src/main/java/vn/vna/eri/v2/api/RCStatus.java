package vn.vna.eri.v2.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.schema.DCServerStatus;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.services.SVDiscord;
import vn.vna.eri.v2.utils.UTApiResponse;
import vn.vna.eri.v2.utils.helper.ResponseCode;

@RestController
public class RCStatus {

  @GetMapping("/api/status/server")
  public ResponseEntity<String> getServerStatus() {
    return UTApiResponse.responseJson(ResponseCode.OK, DCServerStatus.getStatus());
  }

  @GetMapping("/api/status/service/api")
  public ResponseEntity<String> getApiServiceStatus() {
    return UTApiResponse.responseJson(ResponseCode.OK, SVApiControl.getInstance().getStatus());
  }

  @GetMapping("/api/status/service/discord")
  public ResponseEntity<String> getDiscordServiceStatus() {
    return UTApiResponse.responseJson(ResponseCode.OK, SVDiscord.getInstance().getStatus());
  }
}

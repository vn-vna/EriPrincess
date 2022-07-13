package vn.vna.eri.v2.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.vna.eri.v2.clients.ServerConfigClient;
import vn.vna.eri.v2.schema.ServerConfigInfo;
import vn.vna.eri.v2.utils.ApiResponse;
import vn.vna.eri.v2.utils.JsonClass;

@RestController
public class ServerConfigRestController {

  @Autowired
  private ServerConfigClient serverConfigClient;

  @GetMapping("/api/config")
  public ResponseEntity<String> getConfig(@RequestParam String key) {
    Long startCounter = System.nanoTime();
    ServerConfigApiResponse apiResponse = new ServerConfigApiResponse();

    try {
      var result = this.serverConfigClient.getConfig(key);
      apiResponse.setResult(result);
      apiResponse.setSuccess(true);
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - startCounter);
    return ApiResponse.responseJson(ApiResponse.STATUS_OK, apiResponse);
  }

  @DeleteMapping("/api/config")
  public ResponseEntity<String> deleteConfig(@RequestParam String key) {
    Long startCounter = System.nanoTime();
    ServerConfigApiResponse apiResponse = new ServerConfigApiResponse();

    try {
      this.serverConfigClient.removeConfig(key);
      apiResponse.setSuccess(true);
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - startCounter);
    return ApiResponse.responseJson(ApiResponse.STATUS_OK, apiResponse);
  }

  @PutMapping("/api/config")
  public ResponseEntity<String> putConfig(@RequestParam String key, @RequestParam String value) {
    Long startCounter = System.nanoTime();
    ServerConfigApiResponse apiResponse = new ServerConfigApiResponse();

    var result = this.serverConfigClient.setConfig(key, value);
    apiResponse.setResult(result);
    apiResponse.setSuccess(true);

    apiResponse.setTook(System.nanoTime() - startCounter);
    return ApiResponse.responseJson(ApiResponse.STATUS_OK, apiResponse);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ServerConfigApiResponse extends JsonClass {
    private Boolean success;
    private String error;
    private Long took;
    private ServerConfigInfo result;
  }

}

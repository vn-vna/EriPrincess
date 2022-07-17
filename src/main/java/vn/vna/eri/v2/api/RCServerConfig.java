package vn.vna.eri.v2.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.clients.CLServerConfig;
import vn.vna.eri.v2.schema.ARServerConfig;
import vn.vna.eri.v2.schema.DCServerConfigInfo;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.utils.UTApiResponse;

@RestController
public class RCServerConfig {

  @Autowired
  private CLServerConfig serverConfigClient;

  public static RCServerConfig getServerConfigRestController() {
    return SVApiControl.getApplicationContext().getBean(RCServerConfig.class);
  }

  @GetMapping("/api/config/server")
  public ResponseEntity<String> getConfig(@RequestParam String key) {
    Long startCounter = System.nanoTime();
    ARServerConfig apiResponse = new ARServerConfig();

    try {
      DCServerConfigInfo result = this.serverConfigClient.getConfig(key);
      apiResponse.setResult(result);
      apiResponse.setSuccess(true);
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - startCounter);
    return UTApiResponse.responseJson(UTApiResponse.STATUS_OK, apiResponse);
  }

  @DeleteMapping("/api/config/server")
  public ResponseEntity<String> deleteConfig(@RequestParam String key) {
    Long startCounter = System.nanoTime();
    ARServerConfig apiResponse = new ARServerConfig();

    try {
      this.serverConfigClient.removeConfig(key);
      apiResponse.setSuccess(true);
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - startCounter);
    return UTApiResponse.responseJson(UTApiResponse.STATUS_OK, apiResponse);
  }

  @PutMapping("/api/config/server")
  public ResponseEntity<String> putConfig(@RequestParam String key, @RequestParam String value) {
    Long startCounter = System.nanoTime();
    ARServerConfig apiResponse = new ARServerConfig();

    DCServerConfigInfo result = this.serverConfigClient.setConfig(key, value);
    apiResponse.setResult(result);
    apiResponse.setSuccess(true);

    apiResponse.setTook(System.nanoTime() - startCounter);
    return UTApiResponse.responseJson(UTApiResponse.STATUS_OK, apiResponse);
  }

}

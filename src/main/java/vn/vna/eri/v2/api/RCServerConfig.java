package vn.vna.eri.v2.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.clients.CLServerConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;
import vn.vna.eri.v2.schema.ARReloadConfig;
import vn.vna.eri.v2.schema.ARServerConfigManagement;
import vn.vna.eri.v2.schema.DCServerConfigInfo;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.utils.UTApiResponse;
import vn.vna.eri.v2.utils.UTSingleton;
import vn.vna.eri.v2.utils.helper.ResponseCode;

@RestController
public class RCServerConfig {

  public static final String DSC_RT_ALL = "all";
  public static final String DSC_RT_MESSAGE_UTIL = "msg-ult";

  protected Map<String, Class<? extends UpdatableConfigTarget>> reloadTarget;

  @Autowired
  private CLServerConfig serverConfigClient;

  public RCServerConfig() {
  }

  public static RCServerConfig getServerConfigRestController() {
    return SVApiControl.getApplicationContext().getBean(RCServerConfig.class);
  }

  @GetMapping("/api/config/server")
  public ResponseEntity<String> getConfig(@RequestParam String key) {
    Long startCounter = System.nanoTime();
    ARServerConfigManagement apiResponse = new ARServerConfigManagement();

    try {
      this.serverConfigClient.getConfig(key).ifPresent((t) -> {
        apiResponse.setResult(t);
        apiResponse.setSuccess(true);
      });
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - startCounter);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @DeleteMapping("/api/config/server")
  public ResponseEntity<String> deleteConfig(@RequestParam String key) {
    Long startCounter = System.nanoTime();
    ARServerConfigManagement apiResponse = new ARServerConfigManagement();

    try {
      this.serverConfigClient.removeConfig(key);
      apiResponse.setSuccess(true);
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - startCounter);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @PutMapping("/api/config/server")
  public ResponseEntity<String> putConfig(@RequestParam String key, @RequestParam String value) {
    Long startCounter = System.nanoTime();
    ARServerConfigManagement apiResponse = new ARServerConfigManagement();

    DCServerConfigInfo result = this.serverConfigClient.setConfig(key, value);
    apiResponse.setResult(result);
    apiResponse.setSuccess(true);

    apiResponse.setTook(System.nanoTime() - startCounter);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @GetMapping("/api/config/discord/reload")
  public ResponseEntity<String> requestReloadConfig(@RequestParam String target) {
    ResponseCode status = null;
    ARReloadConfig response = new ARReloadConfig();

    if (DSC_RT_ALL.equals(target)) {
      this.reloadTarget.forEach((key, targetClass) -> {
        UTSingleton
            .getInstanceOf(targetClass)
            .ifPresent(UpdatableConfigTarget::update);
      });
      status = ResponseCode.OK;
    } else if (this.reloadTarget.containsKey(target)) {
      UTSingleton
          .getInstanceOf(this.reloadTarget.get(target))
          .ifPresent(UpdatableConfigTarget::update);
      status = ResponseCode.OK;
    } else {
      status = ResponseCode.BAD_REQUEST;
    }

    return UTApiResponse.responseJson(status, response);
  }

}

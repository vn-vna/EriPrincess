package vn.vna.eri.v2.api;

import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.clients.CLServerConfig;
import vn.vna.eri.v2.configs.CFGlobalConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;
import vn.vna.eri.v2.schema.ARReloadConfig;
import vn.vna.eri.v2.schema.ARServerConfigManagement;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.utils.UTApiResponse;
import vn.vna.eri.v2.utils.UTSingleton;
import vn.vna.eri.v2.utils.helper.ResponseCode;

@RestController
public class RCServerConfig {

  public static final String DSC_RT_ALL = "all";
  private static final Logger logger = LoggerFactory.getLogger(RCServerConfig.class);

  protected Map<String, Class<? extends UpdatableConfigTarget>> reloadTarget;

  @Autowired
  private CLServerConfig serverConfigClient;

  public RCServerConfig() {
    this.reloadTarget = CFGlobalConfig.getInstance().getConfigTargets();
  }

  public static RCServerConfig getServerConfigRestController() {
    return SVApiControl
        .getApplicationContext()
        .getBean(RCServerConfig.class);
  }

  @GetMapping("/api/config/server")
  public ResponseEntity<String> getConfig(@RequestParam(required = false) String key) {
    Long beginTime = System.nanoTime();
    ARServerConfigManagement apiResponse = new ARServerConfigManagement();
    apiResponse.setSuccess(false);

    if (Objects.nonNull(key)) {
      this.serverConfigClient.getConfig(key).ifPresentOrElse((result) -> {
        apiResponse.setSuccess(true);
        apiResponse.getResults().add(result);
      }, () -> apiResponse.setError("No configuration found for key [%s]".formatted(key)));
    } else {
      apiResponse.setSuccess(true);
      apiResponse.setResults(this.serverConfigClient.getAllConfig());
    }

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @DeleteMapping("/api/config/server")
  public ResponseEntity<String> deleteConfig(@RequestParam String key) {
    long beginTime = System.nanoTime();
    ARServerConfigManagement apiResponse = new ARServerConfigManagement();
    apiResponse.setSuccess(false);

    this.serverConfigClient
        .removeConfig(key)
        .ifPresentOrElse((result) -> {
          apiResponse.setSuccess(true);
          apiResponse.getResults().add(result);
        }, () -> apiResponse.setError("No configuration found for key [%s]".formatted(key)));

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @PutMapping("/api/config/server")
  public ResponseEntity<String> putConfig(
      @RequestParam String key,
      @RequestParam String value) {
    long beginTime = System.nanoTime();
    ARServerConfigManagement apiResponse = new ARServerConfigManagement();

    this.serverConfigClient
        .setConfig(key, value)
        .ifPresentOrElse((result) -> {
          apiResponse.getResults().add(result);
          apiResponse.setSuccess(true);
        }, () -> apiResponse.setError("No configuration found for key [%s]".formatted(key)));

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse
        .responseJson(ResponseCode.OK, apiResponse);
  }

  @GetMapping("/api/config/reload")
  public ResponseEntity<String> requestReloadConfig(@RequestParam String target) {
    ResponseCode status;
    ARReloadConfig response = new ARReloadConfig();

    if (DSC_RT_ALL.equals(target)) {
      logger.warn("Triggered all config reload");
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

package vn.vna.eri.v2.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.schema.ARDiscordGuildConfig;
import vn.vna.eri.v2.utils.UTApiResponse;
import vn.vna.eri.v2.utils.helper.ResponseCode;

@RestController
public class RCDiscordGuildConfig {

  @Autowired
  private CLDiscordGuildConfig client;

  @GetMapping("/api/config/discord/{id}")
  public ResponseEntity<String> getGuildConfigbyId(
      @PathVariable("id") String guildId) {
    Long beginTime = System.nanoTime();
    ARDiscordGuildConfig apiResponse = new ARDiscordGuildConfig();

    try {
      this.client
          .findConfigurationById(guildId)
          .ifPresentOrElse((result) -> {
            apiResponse.setSuccess(true);
            apiResponse.setResult(result);
          }, () -> {
            apiResponse.setSuccess(false);
            apiResponse.setError("No configuration found for id [%s]".formatted(guildId));
          });
    } catch (Exception ex) {
      apiResponse.setSuccess(false);
      apiResponse.setError(ex.getMessage());
    }

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

}

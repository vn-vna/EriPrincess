package vn.vna.eri.v2.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.vna.eri.v2.clients.CLDiscordGuildConfig;
import vn.vna.eri.v2.schema.ARDiscordGuildConfig;
import vn.vna.eri.v2.schema.DCGuildConfig;
import vn.vna.eri.v2.utils.UTApiResponse;
import vn.vna.eri.v2.utils.UTJsonParser;
import vn.vna.eri.v2.utils.helper.ResponseCode;

@RestController
public class RCDiscordGuildConfig {

  @Autowired
  private CLDiscordGuildConfig client;

  @GetMapping("/api/config/discord/{id}")
  public ResponseEntity<String> getGuildConfigbyId(@PathVariable("id") String guildId) {
    long                 beginTime   = System.nanoTime();
    ARDiscordGuildConfig apiResponse = new ARDiscordGuildConfig();
    apiResponse.setSuccess(false);

    this.client.getConfiguration(guildId).ifPresentOrElse((result) -> {
      apiResponse.setSuccess(true);
      apiResponse.getResults().add(result);
    }, () -> {
      apiResponse.setError("No configuration found for id [%s]".formatted(guildId));
    });

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @PutMapping("/api/config/discord/{id}")
  public ResponseEntity<String> putGuildConfigById(@PathVariable("id") String guildId,
      @RequestBody String info) {
    Long                 beginTime   = System.nanoTime();
    ARDiscordGuildConfig apiResponse = new ARDiscordGuildConfig();
    apiResponse.setSuccess(false);

    DCGuildConfig newInfo = UTJsonParser.fromJson(info, DCGuildConfig.class);

    this.client.updateConfig(guildId, newInfo).ifPresentOrElse((result) -> {
      apiResponse.setSuccess(true);
      apiResponse.getResults().add(result);
    }, () -> {
      apiResponse.setError("No configuration found for id [%s]".formatted(guildId));
    });

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @PostMapping("/api/config/discord/{id}")
  public ResponseEntity<String> createGuildConfigById(@PathVariable("id") String guildId) {
    Long                 beginTime   = System.nanoTime();
    ARDiscordGuildConfig apiResponse = new ARDiscordGuildConfig();
    apiResponse.setSuccess(false);

    this.client.createConfig(guildId).ifPresentOrElse((result) -> {
      apiResponse.setSuccess(true);
      apiResponse.getResults().add(result);
    }, () -> {
      apiResponse.setError("Unable to generate configuration for id [%s]".formatted(guildId));
    });

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

  @DeleteMapping("/api/config/discord/{id}")
  public ResponseEntity<String> deleteGuildEntity(@PathVariable("id") String guildId) {
    long                 beginTime   = System.nanoTime();
    ARDiscordGuildConfig apiResponse = new ARDiscordGuildConfig();
    apiResponse.setSuccess(false);

    this.client.deleteConfig(guildId).ifPresentOrElse((result) -> {
      apiResponse.setSuccess(true);
      apiResponse.getResults().add(result);
    }, () -> {
      apiResponse.setError("No configuration found for id [%s]".formatted(guildId));
    });

    apiResponse.setTook(System.nanoTime() - beginTime);
    return UTApiResponse.responseJson(ResponseCode.OK, apiResponse);
  }

}

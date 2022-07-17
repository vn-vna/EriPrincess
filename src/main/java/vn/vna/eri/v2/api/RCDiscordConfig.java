package vn.vna.eri.v2.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RCDiscordConfig {

  @GetMapping("/api/config/discord")
  public void requestReloadConfig() {

  }

}

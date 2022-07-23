package vn.vna.eri.v2.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import vn.vna.eri.v2.db.ETGuildConfig;
import vn.vna.eri.v2.db.RPGuildConfig;
import vn.vna.eri.v2.schema.DCGuildConfigInfo;
import vn.vna.eri.v2.services.SVApiControl;

@Component
public class CLDiscordGuildConfig {

  private static final Logger logger;

  /**
   * Guild config client cache name
   */
  public static final String CL_GC_CACHE_NAME = "discord-guild-config";

  static {
    logger = LoggerFactory.getLogger(CLDiscordGuildConfig.class);
  }

  @Autowired
  private RPGuildConfig repository;

  public CLDiscordGuildConfig() {
    logger.info("Discord guild config client has been initialized");
  }

  public static CLDiscordGuildConfig getClient() {
    return SVApiControl.getApplicationContext().getBean(CLDiscordGuildConfig.class);
  }

  @Cacheable(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public DCGuildConfigInfo findConfigurationById(String guildId) {
    try {
      var result = this.repository.findById(guildId);
      if (result.isPresent()) {
        return result.get().toDataObject();
      }
    } catch (Exception ex) {
      logger.error(
          "Request get guild configuration from database has failed due to error: {}",
          ex.getMessage());
    }

    return null;
  }

  @CacheEvict(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public DCGuildConfigInfo createConfigForId(String guildId) {
    try {
      var saveData = new DCGuildConfigInfo(guildId);

      var saveInfo = new ETGuildConfig();
      saveInfo.importFromDataObject(saveData);

      return this.repository.save(saveInfo).toDataObject();
    } catch (Exception ex) {
      logger.error(
          "Request create new guild configuration from database has failed due to error: {}",
          ex.getMessage());
    }

    return null;
  }

  @CacheEvict(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public DCGuildConfigInfo updateConfigForId(String guildId) {
    return null;
  }

  public RPGuildConfig getRepository() {
    return repository;
  }

}

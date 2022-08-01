package vn.vna.eri.v2.clients;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import vn.vna.eri.v2.db.ETGuildConfig;
import vn.vna.eri.v2.db.RPGuildConfig;
import vn.vna.eri.v2.schema.DCGuildConfig;
import vn.vna.eri.v2.services.SVApiControl;

@Component
public class CLDiscordGuildConfig {

  /**
   * Guild config client cache name
   */
  public static final String CL_GC_CACHE_NAME = "discord-guild-config";

  private static final Logger logger = LoggerFactory.getLogger(CLDiscordGuildConfig.class);

  @Autowired
  private RPGuildConfig repository;

  public CLDiscordGuildConfig() {
    logger.info("Discord guild config client has been initialized");
  }

  public static CLDiscordGuildConfig getClient() {
    return SVApiControl.getApplicationContext().getBean(CLDiscordGuildConfig.class);
  }

  @Cacheable(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public Optional<DCGuildConfig> getConfiguration(String guildId) {
    return this.repository.findById(guildId).map(ETGuildConfig::toDataObject);
  }

  @CacheEvict(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public Optional<DCGuildConfig> createConfig(String guildId) {
    return this.getConfiguration(guildId).or(() -> {
      var saveData = new DCGuildConfig(guildId);

      var saveInfo = new ETGuildConfig();
      saveInfo.importFromDataObject(saveData);

      return Optional.of(this.repository.save(saveInfo).toDataObject());
    });
  }

  @CachePut(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public Optional<DCGuildConfig> updateConfig(String guildId, DCGuildConfig info) {
    return this.getConfiguration(guildId)
        .map((result) -> {
          ETGuildConfig newEntity = new ETGuildConfig();
          newEntity.importFromDataObject(result, true);
          newEntity.importFromDataObject(info, true);

          return this.repository.save(newEntity).toDataObject();
        });
  }

  @CacheEvict(cacheNames = CL_GC_CACHE_NAME, key = "#guildId")
  public Optional<DCGuildConfig> deleteConfig(String guildId) {
    return this.getConfiguration(guildId).map((result) -> {
      this.repository.deleteById(guildId);
      return result;
    });
  }

}

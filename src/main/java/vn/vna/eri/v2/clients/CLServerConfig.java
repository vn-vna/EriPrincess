package vn.vna.eri.v2.clients;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import vn.vna.eri.v2.db.ETServerConfig;
import vn.vna.eri.v2.db.RPServerConfig;
import vn.vna.eri.v2.schema.DCServerConfig;
import vn.vna.eri.v2.services.SVApiControl;
import vn.vna.eri.v2.utils.UTGenericEntity;

/**
 * Server config client is used to perform transaction with server config database. This client is
 * only accessible when {@link vn.vna.eri.v2.services.SVApiControl ApiControl service} is enabled
 * and works fine.
 *
 * @see vn.vna.eri.v2.clients.CLServerConfig#getClient() Get the instance of this client
 */
@Component
public class CLServerConfig {

  /**
   * Client Server config cache name
   */
  public static final String CL_SC_CACHE_NAME = "server-config";
  private static final Logger logger = LoggerFactory.getLogger(CLServerConfig.class);
  @Autowired
  @Getter
  private RPServerConfig repository;

  /**
   * Don't use the constructor to create a new client
   *
   * @see vn.vna.eri.v2.clients.CLServerConfig#getClient() Get the instance from Spring Boot
   */
  public CLServerConfig() {
    logger.info("Initializing server config client");
  }

  /**
   * Get this client instance from Spring Boot
   *
   * @return {@link vn.vna.eri.v2.clients.CLServerConfig} this client instance
   */
  public static CLServerConfig getClient() {
    return SVApiControl
        .getApplicationContext()
        .getBean(CLServerConfig.class);
  }

  /**
   * Delete specific key from server config database and also evict cache of it
   *
   * @param key - the key of the config should be deleted
   * @return The configuration has been deleted
   * @see vn.vna.eri.v2.db.ETServerConfig ServerConfig Entity
   * @see vn.vna.eri.v2.db.RPServerConfig ServerConfig Repository
   */
  @CacheEvict(cacheNames = CL_SC_CACHE_NAME, key = "#key")
  public Optional<DCServerConfig> removeConfig(String key) {
    return this.getConfig(key)
        .map((result) -> {
          this.repository.deleteById(key);
          return result;
        });
  }

  /**
   * Edit the value of the config with specified key and also require an cache eviction to require
   * reload cache if this config value is used again.
   *
   * @param key   - the key of config should be modified
   * @param value - new value
   * @return new config object if the config is modified successfully or null if any error occured
   * @see DCServerConfig ServerConfig Dataclass
   */
  @CacheEvict(cacheNames = CL_SC_CACHE_NAME, key = "#key")
  public Optional<DCServerConfig> setConfig(String key, String value) {
    try {
      var saveValue = new ETServerConfig();
      saveValue.importFromDataObject(new DCServerConfig(key, value));

      return Optional.ofNullable(this.repository.save(saveValue).toDataObject());
    } catch (Exception ex) {
      logger.error(
          "Request set config {} from database has failed due to: {}",
          key, ex.getMessage());
    }
    return Optional.empty();
  }


  public List<DCServerConfig> getAllConfig() {
    return this.repository
        .findAll()
        .stream()
        .map(ETServerConfig::toDataObject)
        .toList();
  }

  /**
   * Get the config object from database
   *
   * @param key - the key of config to get
   * @return the config object
   * @see DCServerConfig ServerConfig Dataclass
   * @see vn.vna.eri.v2.clients.CLServerConfig#getString(String) getString
   */
  @Cacheable(cacheNames = CL_SC_CACHE_NAME, key = "#key")
  public Optional<DCServerConfig> getConfig(String key) {
    return this.repository.findById(key).map(UTGenericEntity::toDataObject);
  }

  /**
   * Better method to quick query a value from database with specific key
   *
   * @param key - the key of config value to get
   * @return value of the config or null if it is not exists
   * @see vn.vna.eri.v2.clients.CLServerConfig#getConfig(String) getConfig
   */
  public String getString(String key) {
    String value = null;

    try {
      Optional<DCServerConfig> info = this.getConfig(key);
      if (info.isPresent()) {
        value = info.get().getValue();
      }
    } catch (Exception ex) {
      logger.error(
          "Request get config {} from database has failed due to: {}",
          key, ex.getMessage());
    }

    return value;
  }

}

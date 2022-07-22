package vn.vna.eri.v2.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import vn.vna.eri.v2.db.ETServerConfig;
import vn.vna.eri.v2.db.RPServerConfig;
import vn.vna.eri.v2.schema.DCServerConfigInfo;
import vn.vna.eri.v2.services.SVApiControl;

@Component
public class CLServerConfig {

  private static final Logger logger = LoggerFactory.getLogger(CLServerConfig.class);

  /**
   * Client Server config cache name
   */
  public static final String CL_SC_CACHE_NAME = "server-config";

  @Autowired
  private RPServerConfig repository;

  public CLServerConfig() {
    logger.info("Server config client has been initialized");
  }

  public static CLServerConfig getClient() {
    return SVApiControl.getApplicationContext().getBean(CLServerConfig.class);
  }

  @CacheEvict(cacheNames = CL_SC_CACHE_NAME, key = "#key")
  public void removeConfig(String key) {
    try {
      this.repository.deleteById(key);
    } catch (Exception ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }
  }

  @CacheEvict(cacheNames = CL_SC_CACHE_NAME, key = "#key")
  public DCServerConfigInfo setConfig(String key, String value) {
    try {
      var saveValue = new ETServerConfig();
      saveValue.importFromDataObject(new DCServerConfigInfo(key, value));

      return this.repository.save(saveValue).toDataObject();
    } catch (Exception ex) {
      logger.error("Request set config to database has failed due to: {}", ex.getMessage());
    }
    return null;
  }

  @Cacheable(cacheNames = CL_SC_CACHE_NAME, key = "#key")
  public DCServerConfigInfo getConfig(String key) {
    try {
      var result = this.repository.findById(key);
      if (result.isPresent()) {
        return result.get().toDataObject();
      }
    } catch (Exception ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }
    return null;
  }

  public String getString(String key) {
    String value = null;

    try {
      value = this.getConfig(key).getValue();
    } catch (Exception ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }

    return value;
  }

  public RPServerConfig getRepository() {
    return this.repository;
  }

}

package vn.vna.eri.v2.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.vna.eri.v2.configs.ConfigManager;
import vn.vna.eri.v2.db.ServerConfigRepository;
import vn.vna.eri.v2.schema.ServerConfigInfo;
import vn.vna.eri.v2.services.ApiService;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Component
public class ServerConfigClient extends ConfigManager {

  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(ServerConfigClient.class);
  }

  @Autowired
  private ServerConfigRepository repository;

  public ServerConfigClient() {
    logger.info("Server config client has been initialized");
  }

  public static ServerConfigClient getClient() {
    return ApiService.getApplicationContext().getBean(ServerConfigClient.class);
  }

  public void removeConfig(String key) {
    try {
      this.repository.deleteById(key);
    } catch (IllegalArgumentException | NullPointerException ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }
  }

  public ServerConfigInfo setConfig(String key, String value) {
    try {
      var saveValue = new ServerConfigRepository.ServerConfig();
      saveValue.setKey(key);
      saveValue.setValue(value);

      return this.repository.save(saveValue).toDataObject();
    } catch (IllegalArgumentException | NullPointerException ex) {
      logger.error("Request set config to database has failed due to: {}", ex.getMessage());
    }
    return null;
  }

  public ServerConfigInfo getConfig(String key) {
    try {
      var result = this.repository.getById(key);
      if (Objects.isNull(result.getKey())) {
        throw new EntityNotFoundException();
      }
      return result.toDataObject();
    } catch (IllegalArgumentException | NullPointerException ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }
    return null;
  }

  @Override
  public String getString(String key) {
    String value = null;

    try {
      value = this.repository.getById(key).getValue();
    } catch (IllegalArgumentException | NullPointerException ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }

    return value;
  }

  public ServerConfigRepository getRepository() {
    return this.repository;
  }

}

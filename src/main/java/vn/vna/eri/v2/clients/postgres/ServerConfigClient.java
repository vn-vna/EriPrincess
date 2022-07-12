package vn.vna.eri.v2.clients.postgres;

import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.vna.eri.v2.configs.ConfigManager;
import vn.vna.eri.v2.db.ServerConfigRepository;
import vn.vna.eri.v2.schema.ServerConfig;
import vn.vna.eri.v2.services.ApiService;

@Component
public class ServerConfigClient extends ConfigManager {

  static {
    logger = LoggerFactory.getLogger(ServerConfigClient.class);
  }

  public ServerConfigClient() {
    logger.info("Server config client has been initialized");
  }

  public void removeConfig(String key) {
    try {
      this.repository.deleteById(key);
    } catch (IllegalArgumentException | NullPointerException ex) {
      logger.error("Request get config from database has failed due to: {}", ex.getMessage());
    }
  }

  public ServerConfig setConfig(String key, String value) {
    try {
      return this.repository.save(new ServerConfigRepository.ServerConfig(key, value)).toDataObject();
    } catch (IllegalArgumentException | NullPointerException ex) {
      logger.error("Request set config to database has failed due to: {}", ex.getMessage());
    }
    return null;
  }

  public ServerConfig getConfig(String key) {
    try {
      var result = this.repository.getById(key);
      if (Objects.isNull(result) || Objects.isNull(result.getKey())) {
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

  public static ServerConfigClient getClient() {
    return ApiService.getApplicationContext().getBean(ServerConfigClient.class);
  }

  private static Logger logger;

  @Autowired
  private ServerConfigRepository repository;

}

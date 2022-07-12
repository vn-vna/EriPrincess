package vn.vna.eri.v2.schema;

import vn.vna.eri.v2.db.ServerConfigRepository;
import vn.vna.eri.v2.utils.KeyValuePair;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ServerConfig extends KeyValuePair<String, String> {

  public ServerConfig(String key, String value) {
    super(key, value);
  }

  public static ServerConfig convertFromEntity(ServerConfigRepository.ServerConfig config) {
    return new ServerConfig(config.getKey(), config.getValue());
  }
}

package vn.vna.eri.v2.schema;

import lombok.EqualsAndHashCode;
import vn.vna.eri.v2.db.ServerConfigRepository;
import vn.vna.eri.v2.utils.KeyValuePair;

@EqualsAndHashCode(callSuper = true)
public class ServerConfigInfo extends KeyValuePair<String, String> {

  public ServerConfigInfo() {
    this.setKey(null);
    this.setValue(null);
  }

  public ServerConfigInfo(String key, String value) {
    super(key, value);
  }

  public static ServerConfigInfo convertFromEntity(ServerConfigRepository.ServerConfig config) {
    return new ServerConfigInfo(config.getKey(), config.getValue());
  }
}

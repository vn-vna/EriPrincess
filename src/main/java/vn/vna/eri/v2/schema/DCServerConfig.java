package vn.vna.eri.v2.schema;

import lombok.Data;
import vn.vna.eri.v2.db.ETServerConfig;
import vn.vna.eri.v2.utils.UTKeyValuePair;

@Data
public class DCServerConfig extends UTKeyValuePair<String, String> {

  public DCServerConfig() {
    this.setKey(null);
    this.setValue(null);
  }

  public DCServerConfig(String key, String value) {
    super(key, value);
  }

  public static DCServerConfig convertFromEntity(ETServerConfig config) {
    return new DCServerConfig(config.getKey(), config.getValue());
  }
}

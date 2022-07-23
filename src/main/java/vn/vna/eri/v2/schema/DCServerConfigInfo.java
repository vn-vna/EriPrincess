package vn.vna.eri.v2.schema;

import lombok.Data;
import vn.vna.eri.v2.db.ETServerConfig;
import vn.vna.eri.v2.utils.UTKeyValuePair;

@Data
public class DCServerConfigInfo extends UTKeyValuePair<String, String> {

  public DCServerConfigInfo() {
    this.setKey(null);
    this.setValue(null);
  }

  public DCServerConfigInfo(String key, String value) {
    super(key, value);
  }

  public static DCServerConfigInfo convertFromEntity(ETServerConfig config) {
    return new DCServerConfigInfo(config.getKey(), config.getValue());
  }
}

package vn.vna.eri.v2.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.schema.DCServerConfig;
import vn.vna.eri.v2.utils.UTGenericEntity;

@Getter
@Setter
@Entity
@Table(name = "_svrcfg")
public class ETServerConfig
  extends UTGenericEntity<DCServerConfig> {

  @Id
  @Column(name = "_key")
  private String key;
  @Column(name = "_val")
  private String value;

  public ETServerConfig() {
    super(DCServerConfig.class);
  }
}

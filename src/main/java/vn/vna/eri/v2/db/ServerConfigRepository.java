package vn.vna.eri.v2.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vna.eri.v2.db.ServerConfigRepository.ServerConfig;
import vn.vna.eri.v2.schema.ServerConfigInfo;
import vn.vna.eri.v2.utils.ConvertableToDataObject;

@Repository
public interface ServerConfigRepository extends JpaRepository<ServerConfig, String> {

  @Getter
  @Setter
  @Entity
  @Table(name = "_svrcfg")
  class ServerConfig extends ConvertableToDataObject<ServerConfigInfo> {

    @Id
    @Column(name = "_key")
    private String key;
    @Column(name = "_val")
    private String value;

    public ServerConfig() {
      super(ServerConfigInfo.class);
    }
  }

}

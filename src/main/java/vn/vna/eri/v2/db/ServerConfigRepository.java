package vn.vna.eri.v2.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vna.eri.v2.db.ServerConfigRepository.ServerConfig;
import vn.vna.eri.v2.schema.ServerConfigInfo;
import vn.vna.eri.v2.utils.ConvertableToDataObject;

import lombok.Getter;
import lombok.Setter;

@Repository
public interface ServerConfigRepository extends JpaRepository<ServerConfig, String> {

  @Getter
  @Setter
  @Entity
  @Table(name = "_svrcfg")
  static public class ServerConfig extends ConvertableToDataObject<ServerConfigInfo> {

    public ServerConfig() {
      super(ServerConfigInfo.class);
    }

    @Id
    @Column(name = "_key")
    private String key;

    @Column(name = "_val")
    private String value;
  }

}

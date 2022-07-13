package vn.vna.eri.v2.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vna.eri.v2.db.GuildConfigRepository.GuildConfig;
import vn.vna.eri.v2.schema.GuildConfigInfo;
import vn.vna.eri.v2.utils.ConvertableToDataObject;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Repository
public interface GuildConfigRepository extends JpaRepository<GuildConfig, String> {

  @Getter
  @Setter
  @Entity
  @Table(name = "_dsc_guilds")
  class GuildConfig extends ConvertableToDataObject<GuildConfigInfo> {
    @Id
    @Nonnull
    @Column(name = "_id")
    private String guildId;
    @Column(name = "_joined_datetime")
    private Instant joinedDateTime;

    public GuildConfig() {
      super(GuildConfigInfo.class);
    }
  }

}

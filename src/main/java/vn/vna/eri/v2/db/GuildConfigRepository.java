package vn.vna.eri.v2.db;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vna.eri.v2.db.GuildConfigRepository.GuildConfig;
import vn.vna.eri.v2.schema.GuildConfigInfo;
import vn.vna.eri.v2.utils.ConvertableToDataObject;

import lombok.Getter;
import lombok.Setter;

@Repository
public interface GuildConfigRepository extends JpaRepository<GuildConfig, String> {

  @Getter
  @Setter
  @Entity
  @Table(name = "_dsc_guilds")
  public static class GuildConfig extends ConvertableToDataObject<GuildConfigInfo> {
    public GuildConfig() {
      super(GuildConfigInfo.class);
    }

    @Id
    @Nonnull
    @Column(name = "_id")
    private String guildId;

    @Column(name = "_joined_datetime")
    private Instant joinedDateTime;
  }

}
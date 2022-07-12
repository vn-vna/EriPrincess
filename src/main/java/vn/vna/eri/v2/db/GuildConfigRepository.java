package vn.vna.eri.v2.db;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.vna.eri.v2.db.GuildConfigRepository.GuildConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Repository
public interface GuildConfigRepository extends JpaRepository<GuildConfig, String> {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Entity
  @Table(name = "_dsc_guilds")
  public static class GuildConfig {
    @Id
    @Nonnull
    @Column(name = "_id")
    private String guildId;
  }

}

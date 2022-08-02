package vn.vna.eri.v2.db;

import java.time.Instant;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import vn.vna.eri.v2.schema.DCGuildConfig;
import vn.vna.eri.v2.utils.UTGenericEntity;

@Getter
@Setter
@Entity
@Table(name = "_dsc_guilds")
@DynamicUpdate
public class ETGuildConfig
  extends UTGenericEntity<DCGuildConfig> {

  @Id
  @Nonnull
  @Column(name = "_id")
  private String  guildId;
  @Column(name = "_joined_datetime")
  private Instant joinedDateTime;
  @Column(name = "_lang")
  private String  language;
  @Column(name = "_tz")
  private Integer timeZone;
  @Column(name = "_airport")
  private String  airportChannel;

  public ETGuildConfig() {
    super(DCGuildConfig.class);
  }
}

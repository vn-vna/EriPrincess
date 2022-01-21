package vn.vna.erivampir.db.h2.dscguild;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "DISCORDGUILD")
public final class DiscordGuildConfig {

  @Id
  @Column(name = "_GUILD_ID", length = 30, unique = true, nullable = false)
  private String guildId;

  @Column(name = "_GUILD_REGISTER_DATE") private Date registeredDate;

  @Column(name = "_GUILD_GMT") private Integer gmt;
}

package vn.vna.erivampir.db.pgsql.dscguild;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "discordguild")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscordGuildConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_index")
    private Integer index;

    @Column(name = "_guild_id")
    private String guildId;

    @Column(name = "_guild_gmt")
    private Integer guildGMT;

    @Column(name = "_guild_register_date")
    private Timestamp guildRegisteredDate;

    @Column(name = "_enable_gm")
    private boolean enableGoodMorning;

    @Column(name = "_enable_gn")
    private boolean enableGoodNight;

    @Column(name = "_enable_lm")
    private boolean enableLonelyMessage;
}

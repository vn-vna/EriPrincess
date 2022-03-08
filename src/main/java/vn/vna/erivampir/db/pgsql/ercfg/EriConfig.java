package vn.vna.erivampir.db.pgsql.ercfg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@EnableTransactionManagement
@Entity
@Table(name = "ericonfig")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EriConfig {
    @Id
    @Column(name = "_cfg_key")
    private String configKey;

    @Column(name = "_cfg_value")
    private String configValue;

    @Column(name = "_cfg_modified")
    private Timestamp configModifiedTimestamp;
}

package vn.vna.erivampir.db.h2.ericfg;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "ERICONFIG")
public final class EriConfig {

    @Id
    @NonNull
    @Column(name = "_CFG_KEY")
    private String key;

    @Column(name = "_CFG_VALUE")
    private String value;

    @Column(name = "_CFG_MODIFIED")
    private Date modified;

}

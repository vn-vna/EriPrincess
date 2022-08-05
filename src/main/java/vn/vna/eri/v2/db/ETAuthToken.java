package vn.vna.eri.v2.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.vna.eri.v2.schema.DCAuthorizeToken;
import vn.vna.eri.v2.utils.UTGenericEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "_tokens")
public class ETAuthToken
  extends UTGenericEntity<DCAuthorizeToken> {

  @Id
  @Column(name = "_token")
  private String token;
  @Column(name = "_permissions")
  private String permissions;

  public ETAuthToken() {
    super(DCAuthorizeToken.class);
  }

}

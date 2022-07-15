package vn.vna.eri.v2.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.vna.eri.v2.db.TokenManagerRepository.Token;
import vn.vna.eri.v2.schema.AuthorizeToken;
import vn.vna.eri.v2.utils.ConvertableToDataObject;

public interface TokenManagerRepository extends JpaRepository<Token, String> {

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = true)
  @Entity
  @Table(name = "_tokens")
  class Token extends ConvertableToDataObject<AuthorizeToken> {

    @Id
    @Column(name = "_token")
    private String token;
    @Column(name = "_permission")
    private String permissions;

    public Token(Class<AuthorizeToken> type) {
      super(type);
    }
  }

}

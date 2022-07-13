package vn.vna.eri.v2.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.vna.eri.v2.db.TokenManagerRepository.Token;
import vn.vna.eri.v2.schema.AuthorizeToken;
import vn.vna.eri.v2.utils.ConvertableToDataObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

public interface TokenManagerRepository extends JpaRepository<Token, String> {

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = true)
  @Entity
  @Table(name = "_tokens")
  public static class Token extends ConvertableToDataObject<AuthorizeToken> {

    public Token(Class<AuthorizeToken> type) {
      super(type);
    }

    @Id
    @Column(name = "_token")
    private String token;
    @Column(name = "_permission")
    private String permissions;
  }

}

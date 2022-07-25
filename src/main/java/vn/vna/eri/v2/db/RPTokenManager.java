package vn.vna.eri.v2.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RPTokenManager
    extends JpaRepository<ETAuthToken, String> {

}

package vn.vna.erivampir.db.h2.ericfg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EriCfgRepository extends JpaRepository<EriConfig, String> {
}

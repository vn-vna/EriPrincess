package vn.vna.erivampir.db.pgsql.ercfg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EriConfigRepository extends JpaRepository<EriConfig, String> {
}
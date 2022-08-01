package vn.vna.eri.v2.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RPGuildConfig
    extends JpaRepository<ETGuildConfig, String> {

}

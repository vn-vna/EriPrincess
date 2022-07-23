package vn.vna.eri.v2.db;

import java.time.Instant;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RPGuildConfig extends JpaRepository<ETGuildConfig, String> {

  Collection<ETGuildConfig> findByGuildIdIn(Collection<String> guildIds);

  Collection<ETGuildConfig> findByGuildIdNotIn(Collection<String> guildIds);

  Integer countByGuildIdIn(Collection<String> guildIds);

  Integer countByGuildIdNotIn(Collection<String> guildIds);

  Collection<ETGuildConfig> findByJoinedDateTimeBetween(Instant from, Instant to);

}

package vn.vna.erivampir.db.pgsql.dscguild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordGuildConfigRepository extends JpaRepository<DiscordGuildConfig, Integer> {
}
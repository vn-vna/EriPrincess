package vn.vna.erivampir.db.h2.dscguild;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DscGuildCfgRepository
    extends JpaRepository<DiscordGuildConfig, String> {}

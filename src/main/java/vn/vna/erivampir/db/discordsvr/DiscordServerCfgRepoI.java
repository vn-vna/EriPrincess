package vn.vna.erivampir.db.discordsvr;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscordServerCfgRepoI extends MongoRepository<DiscordServerConfiguration, String> {
}

package vn.vna.erivampir.db.mongo.discordsvr;

import org.springframework.data.mongodb.repository.MongoRepository;

@Deprecated
public interface DiscordServerCfgRepoI extends MongoRepository<DiscordServerConfiguration, String> {
}

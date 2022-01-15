package vn.vna.erivampir.db.ericfg;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EriCfgRepoI extends MongoRepository<EriConfiguration, String> {
}

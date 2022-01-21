package vn.vna.erivampir.db.mongo.ericfg;

import org.springframework.data.mongodb.repository.MongoRepository;

@Deprecated
public interface EriCfgRepoI extends MongoRepository<EriConfiguration, String> {
}

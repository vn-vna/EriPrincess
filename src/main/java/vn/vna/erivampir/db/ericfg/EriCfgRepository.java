package vn.vna.erivampir.db.ericfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import vn.vna.erivampir.db.MongoDbConfiguration;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Repository
public class EriCfgRepository implements EriCfgRepositoryI {

    Logger logger = LoggerFactory.getLogger(EriCfgRepository.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Collection<EriConfiguration> getAllConfiguration() {
        return mongoTemplate.findAll(EriConfiguration.class);
    }

    @Override
    public EriConfiguration modifyConfiguration(String key, String value) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EriConfiguration.FIELD_KEY).is(key));
        Update update = new Update();
        update.set(EriConfiguration.FIELD_VALUE, value);
        update.set(EriConfiguration.FIELD_LAST_MODIFY, new Date());
        logger.info("REQUEST UPDATE: attribute " + key + " to " + value);
        return mongoTemplate.findAndModify(query, update, EriConfiguration.class);
    }

    @Override
    public EriConfiguration createConfiguration(String key, String value) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EriConfiguration.FIELD_KEY).is(key));
        EriConfiguration prevField = mongoTemplate.findOne(query, EriConfiguration.class);
        if (Objects.isNull(prevField)) {
            EriConfiguration configuration = new EriConfiguration();
            configuration.setKey(key);
            configuration.setValue(value);
            configuration.setLastModify(new Date());
            return mongoTemplate.insert(configuration, MongoDbConfiguration.COLLECTION_ERI_CONFIGURATION);
        }
        return null;
    }

    @Override
    public EriConfiguration findConfiguration(String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EriConfiguration.FIELD_KEY).is(key));
        return mongoTemplate.findOne(query, EriConfiguration.class);
    }

}

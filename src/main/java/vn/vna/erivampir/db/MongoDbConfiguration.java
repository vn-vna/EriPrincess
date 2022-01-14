package vn.vna.erivampir.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoDbConfiguration {

    public static final String DATABASE_NAME                = "eridb";
    public static final String COLLECTION_ERI_CONFIGURATION = "ericfg";
    public static final String COLLECTION_DISCORD_SERVERS   = "discordsvr";

    Logger logger = LoggerFactory.getLogger(MongoDbConfiguration.class);

    @Bean
    public MongoTemplate mongoTemplate(MongoMappingContext context, MongoDatabaseFactory mongoDbFactory) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);

        return mongoTemplate;
    }

}

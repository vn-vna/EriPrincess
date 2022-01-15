package vn.vna.erivampir.db;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import vn.vna.erivampir.EriServer;
import vn.vna.erivampir.ServerConfig;

@Configuration
public class MongoDbConfiguration {

    public static final String DATABASE_NAME                = "eridb";
    public static final String COLLECTION_ERI_CONFIGURATION = "ericfg";
    public static final String COLLECTION_DISCORD_SERVERS   = "discordsvr";

    Logger logger = LoggerFactory.getLogger(MongoDbConfiguration.class);

    @Bean
    public MongoTemplate mongoTemplate() {
        logger.info("Setting up mongo db connection");
        String                 uri               = ServerConfig.getInstance().getConfiguration(ServerConfig.CFG_MONGODB_URI);
        ConnectionString       connectionString  = new ConnectionString(uri);
        MongoDriverInformation driverInformation = MongoDriverInformation.builder().build();
        MongoClientSettings options = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        MongoClient          mongoClient          = MongoClients.create(options, driverInformation);
        MongoDatabaseFactory mongoDatabaseFactory = new SimpleMongoClientDatabaseFactory(mongoClient, DATABASE_NAME);
        return new MongoTemplate(mongoDatabaseFactory);
    }

}

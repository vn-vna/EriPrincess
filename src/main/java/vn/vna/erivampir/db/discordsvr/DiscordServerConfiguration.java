package vn.vna.erivampir.db.discordsvr;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import vn.vna.erivampir.db.MongoDbConfiguration;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = MongoDbConfiguration.COLLECTION_DISCORD_SERVERS)
public class DiscordServerConfiguration {

    @Id
    @Field
    @Indexed(unique = true)
    private String serverID;

    @Field
    private Date registeredTime;

    @Field
    private Integer serverGMT;
}

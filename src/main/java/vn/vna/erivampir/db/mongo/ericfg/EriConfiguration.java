package vn.vna.erivampir.db.mongo.ericfg;

import java.util.Date;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import vn.vna.erivampir.db.mongo.MongoDbConfiguration;

@Deprecated
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = MongoDbConfiguration.COLLECTION_ERI_CONFIGURATION)
public class EriConfiguration {

  @Id @Indexed(unique = true) private String key;

  @Field private String value;

  @Field private Date lastModify;
}

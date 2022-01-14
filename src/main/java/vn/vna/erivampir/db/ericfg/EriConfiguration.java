package vn.vna.erivampir.db.ericfg;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;
import org.springframework.data.mongodb.core.mapping.Field;
import vn.vna.erivampir.db.MongoDbConfiguration;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = MongoDbConfiguration.COLLECTION_ERI_CONFIGURATION)
public class EriConfiguration {

    public static final String FIELD_KEY         = "key";
    public static final String FIELD_VALUE       = "value";
    public static final String FIELD_LAST_MODIFY = "lmod";

    @Field(FIELD_KEY)
    @Indexed(unique = true)
    private String key;

    @Field(FIELD_VALUE)
    private String value;

    @Field(FIELD_LAST_MODIFY)
    private Date   lastModify;

}

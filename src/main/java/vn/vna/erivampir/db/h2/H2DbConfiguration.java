package vn.vna.erivampir.db.h2;

import org.springframework.context.annotation.Configuration;

@Configuration
public class H2DbConfiguration {

    public static final String H2DB_DBURI  = "jdbc:h2:file:~/h2db/eridb";
    public static final String H2DB_DBPATH = "~/h2db/eridb.mv.db";

}

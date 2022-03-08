package vn.vna.erivampir.db.pgsql;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.vna.erivampir.EriServerConfig;

import javax.sql.DataSource;

@Configuration
public class PostgreSQLCfg {

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(EriServerConfig.getInstance().getConfiguration(EriServerConfig.CFG_SPRING_DATASOURCE));
        dataSourceBuilder.username(EriServerConfig.getInstance().getConfiguration(EriServerConfig.CFG_SPRING_DBUSER));
        dataSourceBuilder.password(EriServerConfig.getInstance().getConfiguration(EriServerConfig.CFG_SPRING_DBPWD));

        return dataSourceBuilder.build();
    }

}

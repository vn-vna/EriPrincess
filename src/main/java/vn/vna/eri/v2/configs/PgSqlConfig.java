package vn.vna.eri.v2.configs;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgSqlConfig {

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName("org.postgresql.Driver");
    dataSourceBuilder.url(ConfigManager.getEnvManager().getString(Env.ENV_DATASOURCE));
    dataSourceBuilder.username(ConfigManager.getEnvManager().getString(Env.ENV_DBUSER));
    dataSourceBuilder.password(ConfigManager.getEnvManager().getString(Env.ENV_DBPWD));
    return dataSourceBuilder.build();
  }

}

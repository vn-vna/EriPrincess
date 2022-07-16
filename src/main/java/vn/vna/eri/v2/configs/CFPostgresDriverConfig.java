package vn.vna.eri.v2.configs;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CFPostgresDriverConfig {

  @Bean
  public DataSource getDataSource() {
    CFGlobalConfig cfgManager = CFGlobalConfig.getInstance();
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName("org.postgresql.Driver");
    dataSourceBuilder.url(cfgManager.getString(CFGlobalConfig.ENV_DATASOURCE));
    dataSourceBuilder.username(cfgManager.getString(CFGlobalConfig.ENV_DBUSER));
    dataSourceBuilder.password(cfgManager.getString(CFGlobalConfig.ENV_DBPWD));
    return dataSourceBuilder.build();
  }

}

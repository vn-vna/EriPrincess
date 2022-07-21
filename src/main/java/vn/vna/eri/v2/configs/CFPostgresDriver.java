package vn.vna.eri.v2.configs;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CFPostgresDriver {

  private CFDataSource cfDataSource;

  public CFPostgresDriver() {
    this.cfDataSource = CFDataSource.getInstance();
  }

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder
        .create()
        .driverClassName("org.postgresql.Driver")
        .url(this.cfDataSource.getDataSource())
        .username(this.cfDataSource.getDbUser())
        .password(this.cfDataSource.getDbPassword());

    return dataSourceBuilder.build();
  }

}

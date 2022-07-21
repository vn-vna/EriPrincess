package vn.vna.eri.v2.configs;

import static vn.vna.eri.v2.configs.CFGlobalConfig.ENV_DATASOURCE;
import static vn.vna.eri.v2.configs.CFGlobalConfig.ENV_DBPWD;
import static vn.vna.eri.v2.configs.CFGlobalConfig.ENV_DBUSER;
import static vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage.PRE_START;

import java.util.Objects;
import lombok.Getter;
import vn.vna.eri.v2.configs.helper.ConfigTarget;
import vn.vna.eri.v2.configs.helper.LoadConfig;
import vn.vna.eri.v2.configs.helper.UpdatableConfigTarget;

@Getter
@ConfigTarget(PRE_START)
public class CFDataSource implements UpdatableConfigTarget {

  private static CFDataSource instance;

  @LoadConfig(ENV_DATASOURCE)
  private String dataSource;
  @LoadConfig(ENV_DBUSER)
  private String dbUser;
  @LoadConfig(ENV_DBPWD)
  private String dbPassword;

  public static CFDataSource getInstance() {
    synchronized (CFDataSource.class) {
      if (Objects.isNull(CFDataSource.instance)) {
        CFDataSource.instance = new CFDataSource();
      }
    }
    return CFDataSource.instance;
  }
}

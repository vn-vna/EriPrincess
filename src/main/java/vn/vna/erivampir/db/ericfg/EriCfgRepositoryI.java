package vn.vna.erivampir.db.ericfg;

import java.util.Collection;

public interface EriCfgRepositoryI {
    Collection<EriConfiguration> getAllConfiguration();

    EriConfiguration modifyConfiguration(String key, String value);

    EriConfiguration createConfiguration(String key, String value);

    EriConfiguration findConfiguration(String key);
}

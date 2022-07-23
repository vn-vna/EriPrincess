package vn.vna.eri.v2.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CFCacheManager {

  @Bean
  public CacheManager getCacheManager() {
    return new ConcurrentMapCacheManager();
  }

}

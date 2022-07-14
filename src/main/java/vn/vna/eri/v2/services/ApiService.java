package vn.vna.eri.v2.services;

import java.time.Instant;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import vn.vna.eri.v2.configs.ConfigManager;
import vn.vna.eri.v2.configs.Env;
import vn.vna.eri.v2.error.ApiServiceExists;
import vn.vna.eri.v2.schema.ServiceStatus;

@SpringBootApplication
@EntityScan("vn.vna.eri.v2")
@ComponentScan("vn.vna.eri.v2")
@EnableJpaRepositories("vn.vna.eri.v2.db")
@EnableCaching
@EnableScheduling
public class ApiService {

  private static final Logger logger;
  private static ApplicationContext springAppCtx;
  private static ApiService instance;

  static {
    logger = LoggerFactory.getLogger(ApiService.class);
  }

  private final ServiceStatus status;

  public ApiService() {
    if (!Objects.isNull(ApiService.instance)) {
      throw new ApiServiceExists();
    }
    this.status = new ServiceStatus();
    this.status.setStatus(ServiceStatus.STATUS_ONLINE);
    this.status.setLastStartUp(Instant.now().toString());
    ApiService.instance = this;
  }

  public static ApplicationContext getApplicationContext() {
    return ApiService.springAppCtx;
  }

  public static ApiService getInstance() {
    return ApiService.instance;
  }

  public static void initialize(String[] args) {
    if (ConfigManager.getEnvManager().getBoolean(Env.ENV_DISABLE_API)) {
      logger.warn("Api service is disabled by default");
      return;
    }

    logger.info("Starting SpingBoot service");
    ApiService.springAppCtx = SpringApplication.run(ApiService.class, args);
  }

  public ServiceStatus getStatus() {
    return this.status;
  }
}

package vn.vna.eri.v2.services;

import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.vna.eri.v2.configs.CFGlobalConfig;
import vn.vna.eri.v2.configs.helper.ConfigTargetLoadStage;
import vn.vna.eri.v2.error.ERApiServiceExists;
import vn.vna.eri.v2.schema.DCServiceStatus;

@SpringBootApplication
@EntityScan("vn.vna.eri.v2")
@ComponentScan("vn.vna.eri.v2")
@EnableJpaRepositories("vn.vna.eri.v2.db")
@EnableCaching
@EnableScheduling
public class SVApiControl {

  private static final Logger       logger;
  private static ApplicationContext springAppCtx;
  private static SVApiControl       instance;

  static {
    logger = LoggerFactory.getLogger(SVApiControl.class);
  }

  @Getter
  private final DCServiceStatus status;

  public SVApiControl() {
    if (!Objects.isNull(SVApiControl.instance)) {
      throw new ERApiServiceExists();
    }
    this.status = new DCServiceStatus();
    this.status.setStatus(DCServiceStatus.STATUS_ONLINE);
    this.status.setLastStartUp(Instant.now().toString());
    SVApiControl.instance = this;
  }

  public static ApplicationContext getApplicationContext() {
    return SVApiControl.springAppCtx;
  }

  public static SVApiControl getInstance() {
    return SVApiControl.instance;
  }

  public static void initialize(String[] args) {
    if (CFGlobalConfig.getInstance().getBoolean(CFGlobalConfig.ENV_DISABLE_API).orElse(false)) {
      logger.warn("Api service is disabled by default");
      return;
    }

    logger.info("Starting SpingBoot service");
    SVApiControl.springAppCtx = SpringApplication.run(SVApiControl.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onReady() {
    logger.info("Spring boot application is initialized successfully");
    CFGlobalConfig.getInstance().invokeUpdateAtStage(ConfigTargetLoadStage.SPRING_SERVICE_READY);
  }

}

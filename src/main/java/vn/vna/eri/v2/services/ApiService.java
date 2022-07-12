package vn.vna.eri.v2.services;

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

import vn.vna.eri.v2.error.ApiServiceExists;

@SpringBootApplication
@EntityScan("vn.vna.eri.v2")
@ComponentScan("vn.vna.eri.v2")
@EnableJpaRepositories("vn.vna.eri.v2.db")
@EnableCaching
@EnableScheduling
public class ApiService {

  static {
    logger = LoggerFactory.getLogger(ApiService.class);
  }

  public ApiService() {
    if (!Objects.isNull(ApiService.instance)) {
      throw new ApiServiceExists();
    }
    ApiService.instance = this;
  }

  public static ApplicationContext getApplicationContext() {
    return ApiService.springAppCtx;
  }

  public static ApiService getInstance() {
    return ApiService.instance;
  }

  public static void initialize(String[] args) {
    logger.info("Starting SpingBoot service");
    ApiService.springAppCtx = SpringApplication.run(ApiService.class, args);
  }

  private static ApplicationContext springAppCtx;
  private static Logger logger;
  private static ApiService instance;
}

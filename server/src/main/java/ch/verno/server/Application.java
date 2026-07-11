package ch.verno.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ch.verno.server")
@EnableJpaRepositories(basePackages = "ch.verno.db.jpa")
@EntityScan(basePackages = "ch.verno.db.entity")
public class Application {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
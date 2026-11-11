package springboot.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Spring application.
 */
@SpringBootApplication
public class EventManagerApplication {

  /**
   * Starts the Spring Boot application.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(EventManagerApplication.class, args);
  }
}

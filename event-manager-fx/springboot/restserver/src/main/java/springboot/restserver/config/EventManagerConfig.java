package springboot.restserver.config;

import event.EventManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.UserManager;

/**
 * Creates the EventManager and UserManager that will be used by the server.
 * This will be used by all API requests,
 * and is automatically managed by Spring to let the Service use it.
 * The file paths used are received from the application.properties file,
 * so they can easily be changed if needed.
 */

@Configuration
public class EventManagerConfig {

  /**
   * Creates the {@link EventManager} used by the whole server.
   *
   * @param path path to the event data file, read from the property {@code eventmanager.event-file}
   * @return the event manager, backed by the event data file
   */
  @Bean
  public EventManager eventManager(@Value("${eventmanager.event-file}") String path) {
    return new EventManager(new EventFileManager(path));
  }

  /**
   * Creates the {@link UserManager} used by the whole server.
   *
   * @param path path to the user data file, read from the property {@code eventmanager.user-file}
   * @return the user manager, backed by the user data file
   */
  @Bean
  public UserManager userManager(@Value("${eventmanager.user-file}") String path) {
    return new UserManager(new UserFileManager(path));
  }
}

package springboot.restserver.config;

import event.EventManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import persistence.EventFileManager;
import persistence.UserFileManager;
import user.UserManager;

@Configuration
public class EventManagerConfig {

    /**
     * Creates the {@link EventManager} used by the whole server.
     * @param path path to the event data file, read from the property
     *             {@code eventmanager.event-file}
     * @return the event manager, backed by the event data file
     */
    @Bean
    public EventManager eventManager(@Value("${eventmanager.event-file}") String path) {
        return new EventManager(new EventFileManager(path));
    }

    @Bean
    public UserManager userManager(@Value("${eventmanager.user-file}") String path) {
        return new UserManager(new UserFileManager(path));
    }
}
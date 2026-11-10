package springboot.restserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EventManagerApplicationTest {

    /**
     * springboot should not throw on startup
     */
    @Test
    void mainStartsApplication() {
        assertDoesNotThrow(() ->
                EventManagerApplication.main(new String[] {"--spring.main.web-application-type=none"})
        );
    }
}
package springboot.restserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EventManagerEventApiTest {

    private static final Path DATA_DIRECTORY = createDataDirectory();

    @Autowired private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureDataFiles(DynamicPropertyRegistry registry) {
        registry.add("eventmanager.event-file", () -> DATA_DIRECTORY.resolve("events.json").toString());
        registry.add("eventmanager.user-file", () -> DATA_DIRECTORY.resolve("users.json").toString());
    }

    private static Path createDataDirectory() {
        try {
            Path directory = Files.createTempDirectory("restserver-test-");
            Files.writeString(directory.resolve("events.json"), "{}");
            Files.writeString(directory.resolve("users.json"), "{}");
            return directory;
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @BeforeEach
    void resetDataFiles() throws Exception {
        Files.writeString(DATA_DIRECTORY.resolve("events.json"), "{}");
        Files.writeString(DATA_DIRECTORY.resolve("users.json"), "{}");
    }
}

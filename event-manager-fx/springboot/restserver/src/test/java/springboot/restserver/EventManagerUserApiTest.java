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
class EventManagerUserApiTest {

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

    private void createTestUser(String username, String password) throws Exception {
        mockMvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
            .andExpect(status().isOk());
    }

    private void createMockEventForUser(String username, String eventId) throws Exception {
        String event = "{\"eventId\":\"" + eventId + "\",\"title\":\"Anniversary\",\"place\":\"Oslo\",\"time\":\"2030-05-01T12:00:00\"}";
        mockMvc.perform(post("/api/events").header("Authorization", username)
                .contentType("application/json").content(event))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventId").value(eventId)); // Use the parameter here
    }


    @BeforeEach
    void resetDataFiles() throws Exception {
        Files.writeString(DATA_DIRECTORY.resolve("events.json"), "{}");
        Files.writeString(DATA_DIRECTORY.resolve("users.json"), "{}");
    }

    /**
     * Tests successful user registration by sending a valid username and password payload
     * and verifying that an OK status is returned along with the expected user identifier and success status.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void registerTest() throws Exception {
        mockMvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content("{\"username\": \"ola\", \"password\": \"banan\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("ola"))
            .andExpect(jsonPath("$.success").value(true));
    }

    /**
     * Tests successful user login using valid credentials after creating a pre-registered test user,
     * ensuring it returns an OK status with the corresponding user ID and success indicator.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void loginTest() throws Exception {
        createTestUser("simba", "tree");
        mockMvc.perform(post("/api/users/login")
                .contentType("application/json")
                .content("{\"username\": \"simba\", \"password\": \"tree\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("simba"))
            .andExpect(jsonPath("$.success").value(true));
    }

    /**
     * Tests the deletion of an existing authenticated user account, verifying that a No Content status is returned.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void deleteUser() throws Exception {
        createTestUser("olav", "blue");
        mockMvc.perform(delete("/api/users/me")
                .header("Authorization", "olav"))
            .andExpect(status().isNoContent());
    }

    /**
     * Tests attempting to delete a user account that does not exist, expecting a Not Found status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void deleteNonExistingUser() throws Exception {
        mockMvc.perform(delete("/api/users/me")
                .header("Authorization", "olav"))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests a user opting in to an existing event successfully, verifying a No Content status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void optInTest() throws Exception {
        createTestUser("oskar", "habanero");
        createMockEventForUser("oskar", "party1");
        createTestUser("olav", "blue");
        mockMvc.perform(put("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isNoContent());
    }

    /**
     * Tests attempting to opt in to an event that does not exist, expecting a Not Found status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void optInNonExistingEventTest() throws Exception {
        mockMvc.perform(put("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests attempting to opt in twice to the same event, expecting a Conflict status response on the second attempt.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void optInTwiceTest() throws Exception {
        createTestUser("oskar", "habanero");
        createMockEventForUser("oskar", "party1");
        createTestUser("olav", "blue");
        mockMvc.perform(put("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isNoContent());
        mockMvc.perform(put("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isConflict());
    }

    /**
     * Tests successfully opting out of an event after having previously opted in, verifying a No Content status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void optOutTest() throws Exception {
        createTestUser("oskar", "habanero");
        createMockEventForUser("oskar", "party1");
        createTestUser("olav", "blue");
        mockMvc.perform(put("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isNoContent());
    }

    /**
     * Tests attempting to opt out of an event that does not exist, expecting a Not Found status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void optOutOfNonExistingEventTest() throws Exception {
        createTestUser("oskar", "habanero");
        mockMvc.perform(delete("/api/users/me/events/party1").header("Authorization", "oskar"))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests attempting to opt out of an event the user is not currently attending, expecting a Conflict status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void optOutWithoutAttendingTest() throws Exception {
        createTestUser("oskar", "habanero");
        createMockEventForUser("oskar", "party1");
        createTestUser("olav", "blue");
        mockMvc.perform(delete("/api/users/me/events/party1").header("Authorization", "olav"))
            .andExpect(status().isConflict());
    }

    /**
     * Tests retrieving the list of events a user has opted into, verifying an OK status and the expected JSON mapping.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void getUserEventsTest() throws Exception {
        createTestUser("oskar", "habanero");
        createMockEventForUser("oskar", "party1");

        mockMvc.perform(get("/api/users/me/events")
                .header("Authorization", "oskar"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"events\":{\"party1\":true}}"));
    }

    /**
     * Tests attempting to retrieve events for a non-existing or unauthenticated user, expecting an Unauthorized status response.
     *
     * @throws Exception if a MockMvc request or assertion fails.
     */
    @Test
    void getNonExistingUserEvents() throws Exception {
        mockMvc.perform(get("/api/users/me/events")
                .header("Authorization", "oskar"))
            .andExpect(status().isUnauthorized());
    }
}

package springboot.restserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the event REST API.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EventManagerEventApiTest {

  private static final Path DATA_DIRECTORY = createDataDirectory();

  @Autowired
  private MockMvc mockMvc;

  /**
   * Configures the application to use temporary data files for this test class.
   *
   * @param registry registry used to provide dynamic Spring properties
   */
  @DynamicPropertySource
  static void configureDataFiles(DynamicPropertyRegistry registry) {
    registry.add(
            "eventmanager.event-file",
            () -> DATA_DIRECTORY.resolve("events.json").toString());
    registry.add(
            "eventmanager.user-file",
            () -> DATA_DIRECTORY.resolve("users.json").toString());
  }

  /**
   * Creates temporary event and user data files for the test application.
   *
   * @return the directory containing the temporary data files
   * @throws ExceptionInInitializerError if the directory or files cannot be created
   */
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

  /**
   * Resets the temporary data files to their initial values before each test.
   *
   * @throws IOException if writing either data file fails
   */
  @BeforeEach
  void resetDataFiles() throws IOException {
    Files.writeString(
            DATA_DIRECTORY.resolve("events.json"),
            "{\"test-1\":{\"eventId\":\"test-1\",\"title\":\"Svomming\","
                    + "\"place\":\"Pirbadet\",\"time\":\"2026-12-01T18:00\"},"
                    + "\"test-2\":{\"eventId\":\"test-2\",\"title\":\"Konsert\","
                    + "\"place\":\"Trondheim Spektrum\",\"time\":\"2026-12-05T20:00\"}}");
    Files.writeString(
            DATA_DIRECTORY.resolve("users.json"),
            "{\"testbruker\":{\"eventIdList\":[],\"username\":\"testbruker\","
                    + "\"password\":\"test123\"}}");
  }

  /**
   * Verifies that an existing event can be retrieved and a missing event returns 404.
   *
   * @throws Exception if a MockMvc request or assertion fails.
   */
  @Test
  void getEventByIdTest() throws Exception {
    mockMvc.perform(get("/api/events/test-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventId").value("test-1"))
            .andExpect(jsonPath("$.title").value("Svomming"))
            .andExpect(jsonPath("$.place").value("Pirbadet"))
            .andExpect(jsonPath("$.time").value("2026-12-01T18:00:00"));

    mockMvc.perform(get("/api/events/notFoundEvent"))
            .andExpect(status().isNotFound());
  }

  /**
   * Verifies that an existing event can be deleted and a missing event returns 404.
   *
   * @throws Exception if a MockMvc request or assertion fails
   */
  @Test
  void deleteEvent() throws Exception {
    mockMvc.perform(delete("/api/events/missing"))
            .andExpect(status().isNotFound());
    mockMvc.perform(delete("/api/events/test-1"))
            .andExpect(status().isNoContent());
  }

  /**
   * Verifies that an event can be created and then retrieved from the API.
   *
   * @throws Exception if a MockMvc request or assertion fails
   */
  @Test
  void createEventSuccessfullyTest() throws Exception {
    String event = "{\"eventId\":\"event-1\",\"title\":\"Demo\","
            + "\"place\":\"Oslo\",\"time\":\"2030-05-01T12:00:00\"}";
    mockMvc.perform(post("/api/events").header("Authorization", "testbruker")
                    .contentType("application/json").content(event))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventId").value("event-1"));

    mockMvc.perform(get("/api/events/event-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Demo"));
  }

  /**
   * Verifies that creating an event with an existing ID returns 409 Conflict.
   *
   * @throws Exception if a MockMvc request or assertion fails
   */
  @Test
  void createdEventAtIdAlreadyExists() throws Exception {
    String event = "{\"eventId\":\"test-1\",\"title\":\"Svomming\","
            + "\"place\":\"Pirbadet\",\"time\":\"2026-12-01T18:00:00\"}";
    mockMvc.perform(post("/api/events").header("Authorization", "testbruker")
                    .contentType("application/json").content(event))
            .andExpect(status().isConflict());
  }

  /**
   * Verifies that the API returns all events.
   *
   * @throws Exception if a MockMvc request or assertion fails
   */
  @Test
  void successfullyRetrieveAllEventsTest() throws Exception {
    mockMvc.perform(get("/api/events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].eventId").value("test-1"))
            .andExpect(jsonPath("$[1].eventId").value("test-2"));
  }

  /**
   * Verifies that an existing event can be updated and the changes are persisted.
   *
   * @throws Exception if a MockMvc request or assertion fails
   */
  @Test
  void successfullyUpdateExistingEventTest() throws Exception {
    String updatedEvent = "{\"eventId\":\"test-1\","
            + "\"title\":\"Oppdatert svømming\",\"place\":\"Trondheim\","
            + "\"time\":\"2026-12-02T19:00:00\"}";

    mockMvc.perform(put("/api/events/test-1")
                    .contentType("application/json")
                    .content(updatedEvent))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventId").value("test-1"))
            .andExpect(jsonPath("$.title").value("Oppdatert svømming"))
            .andExpect(jsonPath("$.place").value("Trondheim"))
            .andExpect(jsonPath("$.time").value("2026-12-02T19:00:00"));

    mockMvc.perform(get("/api/events/test-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Oppdatert svømming"));
  }

  /**
   * Verifies that updating a missing event returns 404 Not Found.
   *
   * @throws Exception if a MockMvc request or assertion fails
   */
  @Test
  void trytoupdateaeventthatdoesntexistTest() throws Exception {
    String missingEvent = "{\"eventId\":\"missing\","
            + "\"title\":\"Oppdatert svømming\",\"place\":\"Trondheim\","
            + "\"time\":\"2026-12-02T19:00:00\"}";

    mockMvc.perform(put("/api/events/missing")
                    .contentType("application/json")
                    .content(missingEvent))
            .andExpect(status().isNotFound());
  }
}
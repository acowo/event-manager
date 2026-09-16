package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import event.Event;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Manages the persistence of {@link Event} objects using a JSON file.
 *
 * <p>Events are stored in memory in a map and synchronized with the
 * {@code eventdata.json} file whenever an event is added, edited, or removed.
 * Gson is used to serialize and deserialize the event data.
 */
public class EventFileManager {

    /**
     * Gson instance configured with an adapter for {@link LocalDateTime}.
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDateTime.class,
                    new LocalDateTimeAdapter()
            )
            .create();

    /**
     * Path to the JSON file containing the event data.
     */
    private static final String filePath = "../data/eventdata.json";

    /**
     * Map containing all currently stored events, indexed by their title.
     */
    private static Map<String, Event> events = loadEvents();

    /**
     * Loads events from the JSON data file.
     *
     * <p>If the file does not exist or is empty, an empty map is returned.
     *
     * @return a map containing the events loaded from the file
     * @throws RuntimeException if the event data file cannot be found
     */
    private static Map<String, Event> loadEvents() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder json = new StringBuilder();

            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }

            if (json.isEmpty()) {
                return new HashMap<>();
            }

            Type type = new TypeToken<Map<String, Event>>() {}.getType();
            Map<String, Event> loadedEvents = gson.fromJson(json.toString(), type);

            return loadedEvents != null ? loadedEvents : new HashMap<>();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find event data file.", e);
        }
    }

    /**
     * Retrieves an event by its title.
     *
     * @param title the title of the event to retrieve
     * @return the event with the specified title
     * @throws IllegalArgumentException if no event with the specified title exists
     */
    public static Event getEvent(String title) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        return events.get(title);
    }

    /**
     * Checks whether an event with the specified title exists.
     *
     * @param title the title of the event to check
     * @return {@code true} if an event with the title exists,
     *         otherwise {@code false}
     */
    public static boolean eventExists(String title) {
        return events.containsKey(title);
    }

    /**
     * Adds an event to the stored events.
     *
     * <p>The event is saved to the JSON file after being added.
     *
     * @param event the event to add
     * @throws IllegalArgumentException if an event with the same title already exists
     */
    public static void addEvent(Event event) {
        if (eventExists(event.getTitle())) {
            throw new IllegalArgumentException(
                    "An event with this title already exists: "
                            + event.getTitle()
            );
        }

        events.put(event.getTitle(), event);
        saveEvents();
    }

    /**
     * Removes an event with the specified title.
     *
     * <p>The updated event collection is saved to the JSON file after removal.
     *
     * @param title the title of the event to remove
     * @throws IllegalArgumentException if no event with the specified title exists
     */
    public static void removeEvent(String title) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        events.remove(title);
        saveEvents();
    }

    /**
     * Replaces an existing event with a new event.
     *
     * <p>The updated event collection is saved to the JSON file after editing.
     *
     * @param title the title of the event to edit
     * @param event the updated event
     * @throws IllegalArgumentException if no event with the specified title exists
     */
    public static void editEvent(String title, Event event) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        events.put(title, event);
        saveEvents();
    }

    /**
     * Retrieves all stored events.
     *
     * <p>A copy of the internal map is returned so that callers cannot
     * directly modify the stored collection.
     *
     * @return a map containing all stored events
     */
    public static Map<String, Event> getEvents() {
        return new HashMap<>(events);
    }

    /**
     * Saves all currently stored events to the JSON data file.
     *
     * @throws RuntimeException if the event data cannot be written to the file
     */
    private static void saveEvents() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(events, writer);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not save event data.",
                    e
            );
        }
    }
}
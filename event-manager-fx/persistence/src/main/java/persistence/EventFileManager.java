package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import event.Event;
import event.EventRepository;

/**
 * Manages the persistence of {@link Event} objects using a JSON file.
 *
 * <p>Events are stored in memory in a map and synchronized with the
 * {@code eventdata.json} file whenever an event is added, edited, or removed.
 * Gson is used to serialize and deserialize the event data.
 */
public class EventFileManager implements EventRepository {

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
    private final String filePath;

    /**
     * Map containing all currently stored events, indexed by their title.
     */
    private Map<String, Event> events;

    /**
     *  creates an EventFilemanager using the default event data file.
     */
    public EventFileManager() {
        this("../data/eventdata.json");
    }

    /**
     * Creates an EventFileManager using the specified file.
     *
     * @param filePath the path to the event data file
     */
    public EventFileManager(String filePath) {
        this.filePath = filePath;
        try {
            this.events = loadEvents();
        } catch (Exception e) {
           this.events = new HashMap<>();
        }

    }
    

    /**
     * Loads events from the JSON data file.
     *
     * <p>If the file does not exist or is empty, an empty map is returned.
     *
     * @return a map containing the events loaded from the file
     * @throws RuntimeException if the event data file cannot be found
     */
    private Map<String, Event> loadEvents() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
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

        } catch (IOException e) {
            throw new RuntimeException("An error has occured while trying to read file.", e);
        }
    }

    
    //Retrieves an event by its title.
    @Override 
    public Event getEvent(String title) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        return events.get(title);
    }


    //Checks whether an event with the specified title exists.
    @Override
    public boolean eventExists(String title) {
        return events.containsKey(title);
    }

    
    //Adds an event to the stored events.
    @Override
    public void addEvent(Event event) {
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
     */
    @Override
    public void removeEvent(String title) {
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
     */
    @Override
    public void editEvent(String title, Event event) {
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
     */
    @Override
    public Map<String, Event> getEvents() {
        return new HashMap<>(events);
    }

    /**
     * Saves all currently stored events to the JSON data file.
     *
     * @throws RuntimeException if the event data cannot be written to the file
     */
    private void saveEvents() {
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            gson.toJson(events, writer);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not save event data.",
                    e
            );
        }
    }

    
    /**
     * Copies this class
     *
     * @return a EventRepository that is a copy
     */
    @Override
    public EventFileManager copy() {
        return new EventFileManager(filePath);
    }
}
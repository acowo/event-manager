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

public class EventFileManager {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDateTime.class,
                    new LocalDateTimeAdapter()
            )
            .create();

    private static final String filePath = "../data/eventdata.json";

    private static Map<String, Event> events = loadEvents();

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

    public static Event getEvent(String title) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        return events.get(title);
    }

    public static boolean eventExists(String title) {
        return events.containsKey(title);
    }

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

    public static void removeEvent(String title) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        events.remove(title);
        saveEvents();
    }

    public static void editEvent(String title, Event event) {
        if (!eventExists(title)) {
            throw new IllegalArgumentException(
                    "Event does not exist: " + title
            );
        }

        events.put(title, event);
        saveEvents();
    }

    public static Map<String, Event> getEvents() {
        return new HashMap<>(events);
    }

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

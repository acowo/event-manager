package persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import user.User;

/**
 * UserFileManager handles the persistence of user data using JSON files.
 * It implements the {@link user.UserRepository} interface to store, retrieve,
 * and update user records locally via Google Gson.
 */
public class UserFileManager implements user.UserRepository {

  private final Gson gson = new Gson();

  private String filePath;

  /**
   * Constructs a new UserFileManager with the default file path.
   */
  public UserFileManager() {
    this("../data/userdata.json");
    checkPath();
  }

  /**
   * Constructs a new UserFileManager with the specified file path.
   *
   * @param filePath the path to the user data file
   */
  public UserFileManager(String filePath) {
    this.filePath = filePath;
    checkPath();
  }

  private void checkPath(){
    if (!new File(filePath).exists()){
      this.filePath = "gr2607/event-manager-fx/data/userdata.json";
    }
  }
  /**
   * Checks if a user by the given username exists.
   *
   * @param username the name of the user in string form
   * @return {@code true} if the user exists; {@code false} otherwise
   */
  @Override
  public boolean userExists(String username) {
    Map<String, User> users = getUsers();
    return users.containsKey(username);
  }

  /**
   * Checks if a specific user exists in a user map.
   *
   * @param users a map of all users containing their username and user object
   * @param username the name of the user in string form
   * @return {@code true} if the user exists; {@code false} otherwise
   */
  protected boolean userExists(Map<String, User> users, String username) {
    return users.containsKey(username);
  }

  /**
   * Finds and returns a user with a specific username.
   *
   * @param username the name of the user in string form
   * @return the user object of the given user
   * @throws IllegalArgumentException if the user does not exist
   */
  @Override
  public User getUser(String username) {
    Map<String, User> users = getUsers();

    if (!users.containsKey(username)) {
      throw new IllegalArgumentException("User doesnt exist");
    }

    return users.get(username);
  }

  /**
   * Checks if a specific username and password combination is valid.
   *
   * @param username the name of the user in string form
   * @param password the password of the user in string form
   * @return {@code true} if the combination of the two fields exists; {@code false} otherwise
   * @throws IllegalArgumentException if the password is null
   */
  // Note for future work, the method should also check if the username is null.
  @Override
  public boolean checkPassword(String username, String password) {
    if (password == null) {
      throw new IllegalArgumentException("Password cannot be null");
    }

    if (!userExists(username)) {
      return false;
    }
    return getUser(username).getPassword().equals(password);
  }

  /**
   * Adds a user to the user map.
   *
   * @param user the user to be added to the user map
   * @throws IllegalArgumentException if the user already exists within the map
   */
  @Override
  public void addUser(User user) {
    Map<String, User> users = getUsers();

    if (userExists(users, user.getUsername())) {
      throw new IllegalArgumentException("User already exists");
    }

    users.put(user.getUsername(), user);
    saveUsers(users);
  }

  /**
   * Edits a user from the user map.
   *
   * @param username the user to be edited in the user map
   * @param user the new version of the user
   * @throws IllegalArgumentException if the user does not exist within the map
   */
  public void editUser(String username, User user) {
    Map<String, User> users = getUsers();

    if (!userExists(users, username)) {
      throw new IllegalArgumentException("User doesn't exists");
    }

    users.put(username, user);
    saveUsers(users);
  }

  /**
   * Creates a copy of this UserFileManager.
   *
   * @return a new UserFileManager with the file path argument
   */
  @Override
  public UserFileManager copy() {
    return new UserFileManager(filePath);
  }

  /**
   * Gets all users from the user map file.
   *
   * @return the map of all user objects with their usernames
   */
  public Map<String, User> getUsers() {

    File file = new File(filePath);
    StringBuilder jsonString = new StringBuilder();

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("Could not create user file", e);
      }
    }

    try (Scanner reader = new Scanner(file, StandardCharsets.UTF_8)) {
      while (reader.hasNextLine()) {
        jsonString.append(reader.nextLine());
      }

    } catch (IOException e) {
      throw new RuntimeException("An error has occurred");
    }

    Map<String, User> users;

    if (jsonString.isEmpty()) {
      users = new HashMap<>();
    } else {
      Type type = new TypeToken<Map<String, User>>() {}.getType();
      users = gson.fromJson(jsonString.toString(), type);
    }

    return users;
  }

  /**
   * Saves all users from the user map to file.
   *
   * @param users the map of users to be saved
   * @throws NullPointerException if the users map is null
   */
  protected void saveUsers(Map<String, User> users) {
    if (users == null) {
      throw new NullPointerException("Users Map cant be null");
    }

    String jsonString = gson.toJson(users);

    File file = new File(filePath);

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("Could not create user file", e);
      }
    }

    try (FileWriter myWriter = new FileWriter(filePath, StandardCharsets.UTF_8)) {
      myWriter.write(jsonString);
    } catch (IOException e) {
      throw new RuntimeException("Could not write to file", e);
    }
  }

  /**
   * Removes an event from all users.
   *
   * @param eventId the event UUID as a string
   */
  @Override
  public void removeEvent(String eventId) {
    Map<String, User> users = getUsers();
    users
        .values()
        .forEach(
            u -> {
              if (u.isAttendingEvent(eventId)) {
                u.removeEventId(eventId);
              }
            });
    saveUsers(users);
  }

  /**
   * Removes a user from the user map.
   *
   * @param username the user to be removed from the user map
   * @throws IllegalArgumentException if the user does not exist within the map
   */
  public void removeUser(String username) {
    Map<String, User> users = getUsers();

    if (!userExists(users, username)) {
      throw new IllegalArgumentException("User doesn't exists");
    }
    users.remove(username);
    saveUsers(users);
  }
}

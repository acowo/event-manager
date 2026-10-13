package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import user.User;

public class UserFileManager implements user.UserRepository {

    private final Gson gson = new Gson();

    private final String filePath;

    public UserFileManager() {
        this("../data/userdata.json");
    }

    public UserFileManager(String filePath) {
        this.filePath = filePath;
    }


    /**
     * Finds and returns a user with a specific username.
     * @param username The name of the user in string form.
     * @return The user object of the given user.
     * @throws IllegalArgumentException if the user does not exist.
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
     * Checks if a user by the given username exists.
     * @param username The name of the user in string form.
     * @return Boolean. True if user exists, false otherwise.
     */
    @Override
    public boolean userExists(String username) {
        Map<String, User> users = getUsers();
        return users.containsKey(username);
    }

    /**
     * Checks if a specific username and password combination is valid.
     * @param username The name of the user in string form.
     * @param password The password of the user in string form.
     * @return Boolean. True if the combination of the two fields exist, false otherwise.
     * @throws IllegalArgumentException if the password is null.
     */

    //Note for future work, the method should also check if the username is null.
    @Override
    public boolean check_password(String username, String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (!userExists(username)) {
            return false;
        }
        return getUser(username).getPassword().equals(password);
    }

    /**
     * Checks if a specific user exists in a user map.
     * @param users A map of all users containing their username and user object.
     * @param username The password of the user in string form.
     * @return Boolean. True if the user exists, false otherwise.
     */
    protected boolean userExists(Map<String, User> users, String username) {
        return users.containsKey(username);
    }

    /**
     * Adds a user to the user map.
     * @param user The user to be added to the user map.
     * @throws IllegalArgumentException If the user already exists within the map.
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
     * Removes a user from the user map.
     * @param username The user to be removed from the user map.
     * @throws IllegalArgumentException If the user does not exist within the map.
     */
    public void removeUser(String username) {
        Map<String, User> users = getUsers();
        
        if (!userExists(users, username)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        users.remove(username);
        saveUsers(users);
    }

    /**
     * Edits a user from the user map.
     * @param username The user to be edited in the user map.
     * @throws IllegalArgumentException If the user does not exist within the map.
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
     * Gets all users, returns the user map.
     * returns The map of all user objects with their usernames.
     * @throws IOException If there is an error while working with the user json file.
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
     * @throws IOException If there is an error while working with the user json file.
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

    @Override
    public void removeEvent(String title) {
        Map<String, User> users = getUsers();
        users.values().forEach(u -> {
            if (u.isAttendingEvent(title)) {
                u.removeEventId(title);
            }
        });
        saveUsers(users);
    }

    /**
     * Creates a new UserFileManager with the filePath variable.
     * returns The new UserFileManager with the filePath variable as the argument.
     */
    @Override 
    public UserFileManager copy() {
        return new UserFileManager(filePath);
    }
}


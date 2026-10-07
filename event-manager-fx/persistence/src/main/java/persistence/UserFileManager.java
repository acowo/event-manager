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



    @Override
    public User getUser(String username) {
         Map<String, User> users = getUsers();

         if (!users.containsKey(username)) {
            throw new IllegalArgumentException("");
         }
         
        return users.get(username);
    }

    @Override
    public boolean userExists(String username) {
        Map<String, User> users = getUsers();
        return users.containsKey(username);
    }

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

    private boolean userExists(Map<String, User> users, String username) {
        return users.containsKey(username);
    }

    @Override
    public void addUser(User user) {
        Map<String, User> users = getUsers();
        
        if (userExists(users, user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        users.put(user.getUsername(), user);
        saveUsers(users);
    }

    public void removeUser(String username) {
        Map<String, User> users = getUsers();
        
        if (!userExists(users, username)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        users.remove(username);
        saveUsers(users);
    }

    public void editUser(String username, User user) {
        Map<String, User> users = getUsers(); 

        if (!userExists(users, username)) {
            throw new IllegalArgumentException("User doesn't exists");
        }

        users.put(username, user);
        saveUsers(users);
    }

    public Map<String, User> getUsers() {
        File file = new File(filePath);

        StringBuilder jsonString = new StringBuilder();


        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            System.out.print("Error while creating file");
            e.printStackTrace();
            } 
        }

        try (Scanner reader = new Scanner(file, StandardCharsets.UTF_8)) {
            while (reader.hasNextLine()) {
                jsonString.append(reader.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.print("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print("An error has occured");
            e.printStackTrace();
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

    private void saveUsers(Map<String, User> users) {
        if (users == null) {
            throw new NullPointerException("Users Map cant be null");
        }

        String jsonString = gson.toJson(users);

        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            System.out.print("Error while creating file");
            e.printStackTrace();
            } 
        }

        try (FileWriter myWriter = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            myWriter.write(jsonString);
            myWriter.flush();
     
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
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

    @Override 
    public UserFileManager copy() {
        return new UserFileManager(filePath);
    }
}


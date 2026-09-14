package persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class UserFileManager {

    public static class User { // placeholder until import
      String userId = "";
      private String getUserId() {
        return userId;
      }
    } 

    private class Data { // For GSON 
        private Map<String, User> users;

        private Map<String, User> convert() {
            return users;
        }
    }

    private static final Gson gson = new Gson();


    private static final String filePath = "./hei.json";

    public static User getUser(String userId) {
        return null;
    }

    private static boolean  userExists(Map<String, User> users, User user) {
        if (users == null) {
            throw new NullPointerException("users cant be null");
        }
        if (user == null) {
            throw new NullPointerException("user cant be null");
        }

        return userExists(users, user.getUserId());
    }

    private static boolean userExists(Map<String, User> users, String userId) {
        if (users == null) {
            throw new NullPointerException("users cant be null");
        }
        if (userId == null) {
            throw new NullPointerException("user cant be null");
        }
        return users.containsKey(userId);
    }

    public static void addUser(User user) {
        Map<String, User> users = getUsers();
        
        if (userExists(users, user)) {
            throw new IllegalArgumentException("User already exists");
        }

        users.put(user.getUserId(), user);
        saveUsers(users);
    }

    public static void removeUser(User user) {
        Map<String, User> users = getUsers(); 
        if (!userExists(users, user)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        users.remove(user.getUserId());
        saveUsers(users);
    }

    public static void removeUser(String userId) {
        Map<String, User> users = getUsers();
        
        if (!userExists(users, userId)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        users.remove(userId);
        saveUsers(users);
    }



    public static Map<String, User> getUsers() {
        File file = new File(filePath);

        String jsonString = "";

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                jsonString += reader.nextLine();
            }

        } catch (FileNotFoundException e) {
            System.out.print("File not found");
            e.printStackTrace();
        }

        Map<String, User> users = gson.fromJson(jsonString, Data.class).convert();

        return users;
    }

    private static void saveUsers(Map<String, User> users) {
        if (users == null) {
            throw new NullPointerException("Users Map cant be null");
        }

        String jsonString = gson.toJson(users);

        File file = new File(filePath);

        try {
            file.createNewFile(); // Make file if it doesnt exist

            FileWriter myWriter = new FileWriter(filePath);

            myWriter.write(jsonString);
            myWriter.flush();
     
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }
}


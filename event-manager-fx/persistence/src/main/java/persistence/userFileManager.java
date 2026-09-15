package persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class UserFileManager {

    public static class User { // placeholder until import
      String username = "";
      private String getUsername() {
        return username;
      }
    } 

    private class Data { // For GSON 
        private Map<String, User> users;

        private Map<String, User> convert() {
            return users;
        }
    }

    private static final Gson gson = new Gson();


    private static final String filePath = "../data/userdata.json";

    public static User getUser(String username) {
         Map<String, User> users = getUsers();

         if (!users.containsKey(username)) {
            throw new IllegalArgumentException("");
         }
         
        return users.get(username);
    }

    public static boolean userExists(String username) {
        Map<String, User> users = getUsers();
        return users.containsKey(username);
    }

    private static boolean userExists(Map<String, User> users, String username) {
        return users.containsKey(username);
    }

    public static void addUser(User user) {
        Map<String, User> users = getUsers();
        
        if (userExists(users, user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        users.put(user.getUsername(), user);
        saveUsers(users);
    }

    public static void removeUser(String username) {
        Map<String, User> users = getUsers();
        
        if (!userExists(users, username)) {
            throw new IllegalArgumentException("User doesn't exists");
        }
        users.remove(username);
        saveUsers(users);
    }

    public static void editUser(String username, User user) {
        Map<String, User> users = getUsers(); 

        if (!userExists(users, username)) {
            throw new IllegalArgumentException("User doesn't exists");
        }

        users.put(username, user);
    }

    public static Map<String, User> getUsers() {
        File file = new File(filePath);

        String jsonString = "";


        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            System.out.print("Error while creating file");
            e.printStackTrace();
            } 
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                jsonString += reader.nextLine();
            }

        } catch (FileNotFoundException e) {
            System.out.print("File not found");
            e.printStackTrace();
        }

        Map<String, User> users;

        if (jsonString.isBlank()) {
            users = new HashMap<>(); 
        } else {
            users = gson.fromJson(jsonString, Data.class).convert();
        }

        

        return users;
    }

    private static void saveUsers(Map<String, User> users) {
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

        try {

            FileWriter myWriter = new FileWriter(filePath);

            myWriter.write(jsonString);
            myWriter.flush();
     
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }
}


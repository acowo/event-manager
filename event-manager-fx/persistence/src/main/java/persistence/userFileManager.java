package persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
        private List<User> users;

        private List<User> convert() {
            return users;
        }
    }

    private static final Gson gson = new Gson();


    private static final String filePath = "./hei.json";

    public static User getUser(String userId) {
        return null;
    }

    private static boolean  userExists(List<User> users, User user) {
        if (users == null) {
            throw new NullPointerException("users cant be null");
        }
        if (user == null) {
            throw new NullPointerException("user cant be null");
        }

        return (users.stream().anyMatch((u) -> u.getUserId().equals(user.getUserId())));
    }

    public static void addUser(User user) {
        List<User> users = getUsers();
        
        if (userExists(users, user)) {
            throw new IllegalArgumentException("User already exists");
        }

        users.add(user);
        saveUsers(users);
    }

    public static void removeUser(User user) {
        List<User> users = getUsers();
        
        if (!userExists(users, user)) {
            throw new IllegalArgumentException("User doesn't exists");
        }

        int n = users.size();

        for (int i = n - 1; i >= 0; i--) {
            if (!users.get(i).getUserId().equals(user.getUserId())) {
                continue;
            }
            users.remove(i);
        }
        saveUsers(users);
    }



    public static List<User> getUsers() {
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

        List<User> users = gson.fromJson(jsonString, Data.class).convert();

        return users;
    }

    private static void saveUsers(List<User> users) {
        String jsonString = gson.toJson(users);

        File file = new File(filePath);

        try {
            file.createNewFile(); // Make file if it doesnt exist

            FileWriter myWriter = new FileWriter(filePath);

            myWriter.write(jsonString);
            myWriter.flush();
     
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();;
        }
    }
}


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

public class UserFileManager {

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

    public static boolean check_password(String username, String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (!userExists(username)) {
            return false;
        }
        return getUser(username).getPassword().equals(password);
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
        saveUsers(users);
    }

    public static Map<String, User> getUsers() {
        File file = new File(filePath);

        StringBuffer jsonString = new StringBuffer();


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
            System.out.print("Error while reading file");
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

    private static void saveUsers(Map<String, User> users) {
        if (users == null) {
            throw new NullPointerException("Users Map cant be null");
        }

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
            gson.toJson(users, myWriter);
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }
}

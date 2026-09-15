package user;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<String> eventIdList = new ArrayList<>();
    private String username;
    private String password;


    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    /**
     * userExists() currently has no real logic
     * @param username
     * @return returns true by default, and false if username is set to false
     */
    public boolean userExists(String username){
        //TODO: Add real logic once UserFileManager is completed
        //The following code is made for testing User-system and is unfinished
        if (username == null){
        throw new NullPointerException("userId cannot be null");
        }
        if (username.equals("false")){
            return false;
        }
        if (username.equals("true")){
            return true;
        }
        return true;
    }

    public static User createUser(String username, String password){
        return new User(username, password);
    }

    public String getUsername(){
      return this.username;
    }

    public String getPassword(){
      return this.password;
    }

    public void setUsername(String username){

      if (username == null){
        throw new NullPointerException("username cannot be null");
      }
      if (username.isBlank()){
        throw new IllegalArgumentException("username cannot be blank");
      }
      this.username = username;
    }

    public void setPassword(String password){
      if (password == null){
        throw new NullPointerException("password cannot be null");
      }
      if (password.isBlank()){
        throw new IllegalArgumentException("password cannot be blank");
      }

      this.password = password; 
    }

    public void addEventId(String eventId){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }
      this.eventIdList.add(eventId);
    }
}




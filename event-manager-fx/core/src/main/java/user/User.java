package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private List<String> eventIdList = new ArrayList<>();
    private String username;
    private String userId;
    private String password;


    public User(String username, String userId, String password){
        setUsername(username);
        setUserId(userId);
        setPassword(password);
    }

    public String getUsername(){
      return this.username;
    }

    public String getUserId(){
      return this.userId;
    }

    public String getPassword(){
      return this.password;
    }
    
    public void setUserId(String userId){

      if (userId == null){
        throw new NullPointerException("userId cannot be null");
      }
      if (userId.isBlank()){
        throw new IllegalArgumentException("userId cannot be blank");
      }
      this.userId = userId;
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
}




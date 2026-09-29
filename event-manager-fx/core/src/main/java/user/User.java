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

    public void removeEventId(String eventId){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }

      if (!this.eventIdList.contains(eventId)){
        throw new IllegalStateException("eventId was not in eventIdList");
      }
      this.eventIdList.remove(eventId);
    }

    public boolean isAttendingEvent(String eventId){
      if (eventId == null){
        throw new NullPointerException("eventId cannot be null");
      }
      if (eventId.isBlank()){
        throw new IllegalArgumentException("eventId cannot be blank");
      }

      return this.eventIdList.contains(eventId);
    }

    public List<String> getEventIdList(){
      return List.copyOf(this.eventIdList);
    }
}




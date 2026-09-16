package user;
import persistence.persistence;


public class UserManager {


    public boolean login(String username, String password){
        //Persistence 
        if (!UserFileManager.check_password(username, password)){
            return false
        }

        UserSession.setUser(UserFileManager.getUser(username));
        
    }

    public boolean register(String username, String password){
        if (!UserFileManager.userExists(username)){
            return false;
        }
        UserSession.setUser(new User(username, password));
    }

}

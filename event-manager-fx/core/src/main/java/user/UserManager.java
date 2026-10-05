package user;



public class UserManager {

    private final UserRepository users;

    public UserManager(UserRepository users){
        this.users = users.copy(); 
    }


    public boolean login(String username, String password){
        //Persistence 
        if (!users.check_password(username, password)){
            return false;
        }

        UserSession.setUser(users.getUser(username));
        return true;
        
    }

    public boolean register(String username, String password){
        if (users.userExists(username)){
            return false;
        }

        User user = new User(username, password);
        users.addUser(user);
        UserSession.setUser(user);
        return true;
    }

}

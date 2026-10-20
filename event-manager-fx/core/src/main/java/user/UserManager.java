package user;


/**
 * 
 * UserManager implements domain logic for managing user aspects:
 * <ul>
 *   <li>Login</li>
 *   <li>Registration</li>
 *   <li>Communicating with persistence</li>
 * </ul>
 */
public class UserManager {

    private final UserRepository users;

    /**
     * Creates a UserManager object from a UserRepository
     * @param users An implementation of a UserRepository
     */
    public UserManager(UserRepository users){
        this.users = users.copy(); 
    }


    /**
     * Logs in using user credentials 
     * @param username the user's username credential
     * @param password the user's password credential
     * @return {@code true} if login was successful;
     *  {@code false} if credentials were incorrect
     */
    public boolean login(String username, String password){
        
        if (!users.check_password(username, password)){
            return false;
        }

        UserSession.setUser(users.getUser(username));
        return true;
        
    }
    /**
     * Creates a new user with the username and password and logs in with the same user credentials
     * @param username the user's username credential
     * @param password the user's password credential
     * @return {@code true} if registration and login was successfull;
     *  {@code false} if user with the same username credential already exists
     * @throws NullPointerException if username is null
     * @throws IllegalArgumentException if username is blank
     */
    public boolean register(String username, String password){
        if (users.userExists(username)){
            return false;
        }

        User user = new User(username, password);
        users.addUser(user);
        UserSession.setUser(user);
        return true;
    }

    /**
     * Edits a user by giving a username to update and an updated User-object
     * @param username the user to be changed
     * @param user the updated user object
     * @return
     */
   public boolean editUser(String username, User user) {
        if (!users.userExists(username)) {return false;}
        users.editUser(username, user);
        return true;
   }

   /**
    * Removes an event from all users
    * @param eventTitle
    */
   public void removeEvent(String eventTitle) {
        users.removeEvent(eventTitle);
   }

}

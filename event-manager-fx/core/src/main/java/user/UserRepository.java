package user;

/**
 * 
 * UserRepository defines the operations for storing and retrieving data from persistence
 */
public interface UserRepository {
    /**
     * Checks if credentials match a user
     * @param username
     * @param password
     * @return {@code true} if credentials are correct;
     *  {@code false} otherwise
     */
    boolean check_password(String username, String password);


    /**
     * Checks if a user with the username credential exists
     * @param username 
     * @return {@code true} user with the username credential exists;
     *  {@code false} 
     * 
     */
    boolean userExists(String username);

    /**
     * Adds a Aser to the repository 
     * @param user User object to be added
     */
    void addUser(User user);

    /**
     * Get a user object with a matching username credential
     * @param username username credential of user to get 
     * @return User object with matching username credential 
     */
    User getUser(String username);

    /**
     * Removes an event from all users
     * @param title
     */
    void removeEvent(String title);

    /**
     * Get a copy of this object 
     * @return Copy of this object
     */
    UserRepository copy();


}

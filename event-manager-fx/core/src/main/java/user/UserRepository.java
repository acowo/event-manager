package user;

public interface UserRepository {
    /**
     * 
     * @param username
     * @param password
     * @return true if a user 
     * @throws IllegalArgumentException() if user does not exist already
     */
    boolean check_password(String username, String password);


    /**
     * 
     * @param username
     * @return false if user does not exist
     * 
     */
    boolean userExists(String username);

    void addUser(User user);

    User getUser(String username);

    /**
     * Removes an event from all users
     * @param title
     */
    void removeEvent(String title);

}

package dto.request;

/**
 * Request payload for logging in or registering a user.
 *
 * @param username the user's chosen username
 * @param password the user password
 */
public record LoginRegisterRequestDto(String username, String password) {
}

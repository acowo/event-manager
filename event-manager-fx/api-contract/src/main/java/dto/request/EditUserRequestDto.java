package dto.request;

/**
 * Request body for editing a user.
 *
 * @param authToken the token identifying the user to edit
 */
public record EditUserRequestDto(String authToken) {
}

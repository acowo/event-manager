package dto.request;

/**
 * Request body for getting events a user is attending..
 *
 * @param userToken the token identifying the user to edit
 */
public record UserGetEventsRequestDto(String userToken) {
}

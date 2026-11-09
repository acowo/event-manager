package dto.response;

/**
 * Record of the response of an attempt to edit user.
 *
 * @param success wether the edit attempt was successful or not.
 */
public record EditUserResponseDto(boolean success) {
}

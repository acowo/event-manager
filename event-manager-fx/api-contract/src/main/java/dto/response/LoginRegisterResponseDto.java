package dto.response;

/**
 * Response payload returned after a login or registration attempt.
 *
 * @param id the unique identifier of the user
 * @param success whether the operation succeeded
 */
public record LoginRegisterResponseDto(String id, boolean success) {
}

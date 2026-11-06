package springboot.restserver.dto;public class EventDto {
    public record UsernameDTO(String username){}

    public record LoginRequestDTO(String username, String password){}

}

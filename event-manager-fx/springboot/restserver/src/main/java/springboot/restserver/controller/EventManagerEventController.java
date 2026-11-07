package springboot.restserver.controller;

import dto.EventDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springboot.restserver.service.EventManagerService;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/events")
public class EventManagerEventController {

  private final EventManagerService eventManagerService;

  public EventManagerEventController(EventManagerService eventManagerService) {
    this.eventManagerService = eventManagerService;
  }

  // ==================== GET MAPPINGS ====================

  @GetMapping
  public List<EventDto> getAll() {
    return eventManagerService.getAll();
  }

  @GetMapping("/{id}")
  public EventDto get(@PathVariable("id") String id) {
    Optional<EventDto> event = eventManagerService.get(id);
    if (event.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return event.get();
  }

  // ==================== POST MAPPING ====================

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EventDto create(@RequestBody EventDto dto) {
    if (!eventManagerService.create(dto)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return dto;
  }

  // ==================== PUT MAPPING (UPDATE) ====================

  @PutMapping("/{id}")
  public EventDto update(@PathVariable("id") String id, @RequestBody EventDto dto) {
    if (!eventManagerService.update(id, dto)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return dto;
  }

  // ==================== DELETE MAPPING ====================

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") String id) {
    if (!eventManagerService.delete(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}

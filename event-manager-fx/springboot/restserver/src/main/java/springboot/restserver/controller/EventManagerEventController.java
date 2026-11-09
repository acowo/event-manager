package springboot.restserver.controller;

import dto.EventDto;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import springboot.restserver.service.EventManagerService;


/**
 * REST controller exposing CRUD operations for events.
 */
@RestController
@RequestMapping("/api/events")
public class EventManagerEventController {

  private final EventManagerService eventManagerService;

  /**
   * Creates a controller backed by the given service.
   *
   * @param eventManagerService the service that handles event operations
   */
  public EventManagerEventController(EventManagerService eventManagerService) {
    this.eventManagerService = eventManagerService;
  }

  // ==================== GET MAPPINGS ====================
  /**
   * Gets all events.
   *
   * @return the list of all events
   */
  @GetMapping
  public List<EventDto> getAll() {
    return eventManagerService.getAll();
  }

  /**
   * Gets an event by its ID.
   *
   * @param id the ID of the event
   * @return the event
   * @throws ResponseStatusException with status 404 if no event with that ID exists
   */
  @GetMapping("/{id}")
  public EventDto get(@PathVariable("id") String id) {
    Optional<EventDto> event = eventManagerService.get(id);
    if (event.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return event.get();
  }

  // ==================== POST MAPPING ====================

  /**
   * Creates a new event.
   *
   * @param dto the event to create
   * @return the created event
   * @throws ResponseStatusException with status 409 if the event already exists
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EventDto create(@RequestBody EventDto dto) {
    if (!eventManagerService.create(dto)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return dto;
  }

  // ==================== PUT MAPPING (UPDATE) ====================

  /**
   * Updates an existing event.
   *
   * @param id the ID of the event to update
   * @param dto the new event data
   * @return the updated event
   * @throws ResponseStatusException with status 404 if no event with that ID exists
   */
  @PutMapping("/{id}")
  public EventDto update(@PathVariable("id") String id, @RequestBody EventDto dto) {
    if (!eventManagerService.update(id, dto)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return dto;
  }

  // ==================== DELETE MAPPING ====================

  /**
   * Deletes an event.
   *
   * @param id the ID of the event to delete
   * @throws ResponseStatusException with status 404 if no event with that ID exists
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") String id) {
    if (!eventManagerService.delete(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}

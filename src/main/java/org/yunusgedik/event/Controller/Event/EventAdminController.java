package org.yunusgedik.event.Controller.Event;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.Event.CreateValidation;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Model.Event.UpdateValidation;
import org.yunusgedik.event.Service.EventService;

@RestController
@RequestMapping("/admin/event")
public class EventAdminController {
    private final EventService eventService;

    EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> create(@Validated(CreateValidation.class) @RequestBody EventDTO eventCreateDTO) {
        Event createdEvent = this.eventService.create(eventCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> update(
        @Validated(UpdateValidation.class) @RequestBody EventDTO eventUpdateDTO,
        @PathVariable Long id
    ) {
        Event updatedEvent = this.eventService.update(id, eventUpdateDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> delete(@RequestParam(name = "id", required = true) Long id) {
        Event deletedEvent = this.eventService.delete(id);
        return ResponseEntity.ok(deletedEvent);
    }
}

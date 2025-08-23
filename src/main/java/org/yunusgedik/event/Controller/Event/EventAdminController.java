package org.yunusgedik.event.Controller.Event;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Service.EventService;

@RestController
@RequestMapping("/event/admin")
public class EventAdminController {
    private final EventService eventService;

    EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public Event create(@RequestBody EventDTO eventDTO){
        return this.eventService.create(eventDTO);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Event update(@RequestBody EventDTO eventDTO, @PathVariable Long id){
        return this.eventService.update(id, eventDTO);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public Event delete(@RequestParam(name = "id", required = true) Long id){
        return this.eventService.delete(id);
    }
}

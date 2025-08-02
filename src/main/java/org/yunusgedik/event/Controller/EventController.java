package org.yunusgedik.event.Controller;

import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Service.EventService;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("")
    public Event get(@RequestParam(name = "id", required = true) Long id){
        return this.eventService.get(id);
    }

    @GetMapping("/all")
    public List<Event> getAll(){
        return this.eventService.getAll();
    }

    @GetMapping("/{id}")
    public Event getByPathVariable(@PathVariable(name = "id" ,required = true) Long id){
        return this.eventService.get(id);
    }

    @PostMapping("/new")
    public Event create(@RequestBody EventDTO eventDTO){
        return this.eventService.create(eventDTO);
    }

    @PatchMapping("/update/{id}")
    public Event update(@RequestBody EventDTO eventDTO, @PathVariable Long id){
        return this.eventService.update(id, eventDTO);
    }

    @DeleteMapping("")
    public Event delete(@RequestParam(name = "id", required = true) Long id){
        return this.eventService.delete(id);
    }
}

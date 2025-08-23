package org.yunusgedik.event.Controller.EventCategory;

import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Service.EventCategoryService;

import java.util.List;

@RestController
@RequestMapping("/event-category")
public class EventCategoryController {
    private final EventCategoryService eventCategoryService;

    public EventCategoryController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @GetMapping("/all")
    public List<EventCategory> getAll() {
        return this.eventCategoryService.getAll();
    }

    @GetMapping
    public EventCategory getById(@RequestParam(name = "id") Long id) {
        return eventCategoryService.get(id);
    }

    @GetMapping("/{id}")
    public EventCategory getByPathVariableId(@PathVariable(name = "id") Long id) {
        return eventCategoryService.get(id);
    }

}

package org.yunusgedik.event.Controller;

import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Service.EventCategoryService;
import org.yunusgedik.event.Service.EventService;

import java.util.List;

@RestController
@RequestMapping("/event-category")
public class EventCategoryController {
    private final EventCategoryService eventCategoryService;

    public EventCategoryController(EventService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @GetMapping("/all")
    public List<EventCategory> getAll() {
        return this.eventCategoryService.getAll();
    }

    @GetMapping("/id")
    public EventCategory getById(@RequestParam(name = "id", required = true) Long id) {
        return eventCategoryService.get(id);
    }

    @GetMapping("/{id}")
    public EventCategory getByPathVariableId(@PathVariable(name = "id", required = true) Long id) {
        return eventCategoryService.get(id);
    }

    @PostMapping("/new")
    public EventCategory create(@RequestBody(required = true) EventCategoryDTO categoryDTO) {
        return this.eventCategoryService.create();
    }

    @PostMapping("/update")
    public EventCategory update(@RequestBody(required = true) EventCategoryDTO categoryDTO) {
        return this.eventCategoryService.update(categoryDTO);
    }

}

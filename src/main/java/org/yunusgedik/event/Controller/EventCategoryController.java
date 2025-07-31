package org.yunusgedik.event.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
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

    @GetMapping("")
    public EventCategory getById(@RequestParam(name = "id") Long id) {
        return eventCategoryService.get(id);
    }

    @GetMapping("/{id}")
    public EventCategory getByPathVariableId(@PathVariable(name = "id") Long id) {
        return eventCategoryService.get(id);
    }

    @PostMapping("/new")
    public EventCategory create(@RequestBody EventCategoryDTO categoryDTO) {
        return this.eventCategoryService.create(categoryDTO);
    }

    @PatchMapping("/update")
    public EventCategory update(@RequestBody EventCategoryDTO categoryDTO) {
        return this.eventCategoryService.update(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventCategory> delete(@PathVariable("id") Long id) {
        EventCategory deleted = this.eventCategoryService.delete(id);

        if (deleted == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(deleted);
    }

}

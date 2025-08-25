package org.yunusgedik.event.Controller.EventCategory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Service.EventCategoryService;

@RestController
@RequestMapping("/admin/event-category")
public class EventCategoryAdminController {
    private final EventCategoryService eventCategoryService;

    public EventCategoryAdminController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public EventCategory create(@RequestBody EventCategoryDTO categoryDTO) {
        return this.eventCategoryService.create(categoryDTO);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public EventCategory update(@RequestBody EventCategoryDTO categoryDTO) {
        return this.eventCategoryService.update(categoryDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventCategory> delete(@PathVariable("id") Long id) {
        EventCategory deleted = this.eventCategoryService.delete(id);

        if (deleted == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(deleted);
    }
}

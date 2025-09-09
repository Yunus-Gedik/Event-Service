package org.yunusgedik.event.Controller.EventCategory;

import jakarta.validation.Valid;
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
    public ResponseEntity<EventCategory> create(
        @Valid @RequestBody EventCategoryDTO categoryCreateDTO
    ) {
        EventCategory createdCategory = this.eventCategoryService.create(categoryCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventCategory> update(
        @Valid @RequestBody EventCategoryDTO categoryUpdateDTO,
        @PathVariable("id") Long id
    ) {
        EventCategory updatedCategory = this.eventCategoryService.update(id, categoryUpdateDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventCategory> delete(@PathVariable("id") Long id) {
        EventCategory deletedCategory = this.eventCategoryService.delete(id);
        return ResponseEntity.ok(deletedCategory);
    }
}

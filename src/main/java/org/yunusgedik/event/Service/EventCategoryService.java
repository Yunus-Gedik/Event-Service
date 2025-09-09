package org.yunusgedik.event.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Repository.EventCategoryRepository;

import java.util.List;

@Service
public class EventCategoryService {

    private final EventCategoryRepository repository;

    public EventCategoryService(EventCategoryRepository repository) {
        this.repository = repository;
    }

    public EventCategory get(Long id) {
        return this.repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "EventCategory not found"));
    }

    public List<EventCategory> getAll() {
        return this.repository.findAll();
    }

    public EventCategory create(EventCategoryDTO categoryCreateDTO) {
        EventCategory eventCategory = new EventCategory();
        eventCategory.setName(categoryCreateDTO.getName());
        return this.repository.save(eventCategory);
    }

    public EventCategory update(Long id, EventCategoryDTO categoryUpdateDTO) {
        EventCategory eventCategory = this.get(id);
        eventCategory.setName(categoryUpdateDTO.getName());
        return this.repository.save(eventCategory);
    }

    public EventCategory delete(Long id) {
        EventCategory eventCategory = this.get(id);
        this.repository.delete(eventCategory);
        return eventCategory;
    }
}

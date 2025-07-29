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
        return this.repository.findById(id).orElse(null);
    }

    public List<EventCategory> getAll() {
        return this.repository.findAll();
    }

    public EventCategory create(EventCategoryDTO categoryDTO) {
        EventCategory eventCategory = new EventCategory();
        eventCategory.setName(categoryDTO.getName());
        if (categoryDTO.getId() == null ||
            (!repository.existsById(categoryDTO.getId()) && categoryDTO.getId() > 0)) {
            return this.repository.save(eventCategory);
        }
        throw new ResponseStatusException(HttpStatus.IM_USED, "ID is in use.");
    }

    public EventCategory update(EventCategoryDTO categoryDTO) {
        EventCategory eventCategory = this.get(categoryDTO.getId());
        eventCategory.setName(categoryDTO.getName());
        return this.repository.save(eventCategory);
    }

    public EventCategory delete(Long id) {
        EventCategory eventCategory = this.get(id);
        if (eventCategory != null) {
            this.repository.delete(eventCategory);
            return eventCategory;
        }
        return null;
    }
}

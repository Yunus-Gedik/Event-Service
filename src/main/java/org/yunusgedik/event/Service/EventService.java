package org.yunusgedik.event.Service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Repository.EventRepository;
import org.yunusgedik.event.Repository.EventCategoryRepository;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.modelMapper = modelMapper;
    }

    public Event get(Long id) {
        return this.eventRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public List<Event> getAll() {
        return this.eventRepository.findAll();
    }

    public Event create(EventDTO eventCreateDTO) {
        Event event = new Event();
        modelMapper.map(eventCreateDTO, event);
        return this.eventRepository.save(event);
    }

    public Event update(Long id, EventDTO eventUpdateDTO) {
        Event event = this.eventRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")
        );
        
        // Only update fields that are not null in the DTO
        if (eventUpdateDTO.getCategoryId() != null) {
            EventCategory category = eventCategoryRepository.findById(eventUpdateDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            event.setCategory(category);
        }
        
        if (eventUpdateDTO.getTitle() != null) {
            event.setTitle(eventUpdateDTO.getTitle());
        }
        if (eventUpdateDTO.getDescription() != null) {
            event.setDescription(eventUpdateDTO.getDescription());
        }
        if (eventUpdateDTO.getLocation() != null) {
            event.setLocation(eventUpdateDTO.getLocation());
        }
        if (eventUpdateDTO.getEventDate() != null) {
            event.setEventDate(eventUpdateDTO.getEventDate());
        }
        if (eventUpdateDTO.getCapacity() != null) {
            event.setCapacity(eventUpdateDTO.getCapacity());
        }
        if (eventUpdateDTO.getPrice() != null) {
            event.setPrice(eventUpdateDTO.getPrice());
        }
        if (eventUpdateDTO.getActive() != null) {
            event.setActive(eventUpdateDTO.getActive());
        }
        
        return this.eventRepository.save(event);
    }

    public Event delete(Long id) {
        Event event = this.eventRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")
        );
        this.eventRepository.delete(event);
        return event;
    }
}

package org.yunusgedik.event.Service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    public Event get(Long id){
        return this.eventRepository.findById(id).orElse(null);
    }

    public List<Event> getAll() {
        return this.eventRepository.findAll();
    }

    public Event create(EventDTO eventDTO) {
        Event event = new Event();
        modelMapper.map(eventDTO, event);
        return this.eventRepository.save(event);
    }

    public Event update(EventDTO eventDTO) {
        Event event = this.eventRepository.findById(eventDTO.getId()).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")
        );
        modelMapper.map(eventDTO, event);
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

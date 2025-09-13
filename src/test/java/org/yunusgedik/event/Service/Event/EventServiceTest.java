package org.yunusgedik.event.Service.Event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Helper.ModelMapperConfig;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Repository.EventCategoryRepository;
import org.yunusgedik.event.Repository.EventRepository;
import org.yunusgedik.event.Helper.MockDataCreator;
import org.yunusgedik.event.Service.EventService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventCategoryRepository eventCategoryRepository;

    private EventService eventService;

    private Event sampleEvent;
    private EventDTO sampleEventDTO;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper();
        eventService = new EventService(eventRepository, eventCategoryRepository, modelMapper);
        sampleEvent = MockDataCreator.createSampleEvent(1L, "Test Event");
        sampleEventDTO = MockDataCreator.createSampleEventDTO();
    }

    @Test
    @DisplayName("Should get event by id successfully")
    void shouldGetEventByIdSuccessfully() {
        // Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(sampleEvent));

        // When
        Event result = eventService.get(eventId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(eventId);
        assertThat(result.getTitle()).isEqualTo("Test Event");
        verify(eventRepository).findById(eventId);
    }

    @Test
    @DisplayName("Should throw exception when event not found by id")
    void shouldThrowExceptionWhenEventNotFoundById() {
        // Given
        Long eventId = 999L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.get(eventId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Event not found");
        verify(eventRepository).findById(eventId);
    }

    @Test
    @DisplayName("Should get all events successfully")
    void shouldGetAllEventsSuccessfully() {
        // Given
        List<Event> events = MockDataCreator.createSampleEventList();
        when(eventRepository.findAll()).thenReturn(events);

        // When
        List<Event> result = eventService.getAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(events);
        verify(eventRepository).findAll();
    }

    @Test
    @DisplayName("Should create event successfully")
    void shouldCreateEventSuccessfully() {
        // Given
        when(eventRepository.save(any(Event.class))).thenReturn(sampleEvent);

        // When
        Event result = eventService.create(sampleEventDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(sampleEvent.getId());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    @DisplayName("Should update event successfully with all fields")
    void shouldUpdateEventSuccessfullyWithAllFields() {
        // Given
        Long eventId = 1L;
        EventDTO updateDTO = MockDataCreator.createUpdateEventDTO();
        updateDTO.setCategoryId(2L);
        
        EventCategory newCategory = MockDataCreator.createSampleEventCategory(2L, "Sports");

        // When
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(sampleEvent));
        when(eventCategoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        when(eventRepository.save(eventCaptor.capture())).thenAnswer(val -> val.getArgument(0));

        Event savedEvent = eventService.update(eventId, updateDTO);

        // Then
        assertThat(savedEvent).isNotNull();
        verify(eventRepository).findById(eventId);
        verify(eventCategoryRepository).findById(2L);


        assertThat(savedEvent.getId()).isEqualTo(sampleEvent.getId());
        assertThat(savedEvent.getCategory()).isEqualTo(newCategory);
        assertThat(savedEvent.getTitle()).isEqualTo(updateDTO.getTitle());
        assertThat(savedEvent.getDescription()).isEqualTo(updateDTO.getDescription());
        assertThat(savedEvent.getLocation()).isEqualTo(updateDTO.getLocation());
        assertThat(savedEvent.getCapacity()).isEqualTo(updateDTO.getCapacity());
        assertThat(savedEvent.getPrice()).isEqualTo(updateDTO.getPrice());
        assertThat(savedEvent.isActive()).isEqualTo(updateDTO.getActive());

        // Fields not provided in DTO should remain unchanged
        assertThat(savedEvent.getEventDate()).isEqualTo(sampleEvent.getEventDate());
        assertThat(savedEvent.getCreatedAt()).isEqualTo(sampleEvent.getCreatedAt());
        assertThat(savedEvent.getLastUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should update event successfully with partial fields")
    void shouldUpdateEventSuccessfullyWithPartialFields() {
        // Given
        Long eventId = 1L;
        EventDTO partialUpdateDTO = MockDataCreator.createPartialUpdateEventDTO();
        
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(sampleEvent));

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        when(eventRepository.save(eventCaptor.capture())).thenAnswer(val -> val.getArgument(0));

        // When
        Event savedEvent = eventService.update(eventId, partialUpdateDTO);

        // Then
        assertThat(savedEvent).isNotNull();
        verify(eventRepository).findById(eventId);
        verify(eventCategoryRepository, never()).findById(anyLong());

        // Only updated fields should be changed
        assertThat(savedEvent.getTitle()).isEqualTo(partialUpdateDTO.getTitle());
        assertThat(savedEvent.getCapacity()).isEqualTo(partialUpdateDTO.getCapacity());

        // Fields not provided in DTO should remain unchanged
        assertThat(savedEvent.getId()).isEqualTo(sampleEvent.getId());
        assertThat(savedEvent.getCategory()).isEqualTo(sampleEvent.getCategory());
        assertThat(savedEvent.getDescription()).isEqualTo(sampleEvent.getDescription());
        assertThat(savedEvent.getLocation()).isEqualTo(sampleEvent.getLocation());
        assertThat(savedEvent.getEventDate()).isEqualTo(sampleEvent.getEventDate());
        assertThat(savedEvent.getPrice()).isEqualTo(sampleEvent.getPrice());
        assertThat(savedEvent.isActive()).isEqualTo(sampleEvent.isActive());
        assertThat(savedEvent.getCreatedAt()).isEqualTo(sampleEvent.getCreatedAt());
        assertThat(savedEvent.getLastUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent event")
    void shouldThrowExceptionWhenUpdatingNonExistentEvent() {
        // Given
        Long eventId = 999L;
        EventDTO updateDTO = MockDataCreator.createUpdateEventDTO();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.update(eventId, updateDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Event not found");
        verify(eventRepository).findById(eventId);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with non-existent category")
    void shouldThrowExceptionWhenUpdatingWithNonExistentCategory() {
        // Given
        Long eventId = 1L;
        EventDTO updateDTO = MockDataCreator.createUpdateEventDTO();
        updateDTO.setCategoryId(999L);
        
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(sampleEvent));
        when(eventCategoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.update(eventId, updateDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Category not found");
        verify(eventRepository).findById(eventId);
        verify(eventCategoryRepository).findById(999L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    @DisplayName("Should delete event successfully")
    void shouldDeleteEventSuccessfully() {
        // Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(sampleEvent));

        // When
        Event result = eventService.delete(eventId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(eventId);
        verify(eventRepository).findById(eventId);
        verify(eventRepository).delete(sampleEvent);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent event")
    void shouldThrowExceptionWhenDeletingNonExistentEvent() {
        // Given
        Long eventId = 999L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventService.delete(eventId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Event not found");
        verify(eventRepository).findById(eventId);
        verify(eventRepository, never()).delete(any(Event.class));
    }

    @Test
    @DisplayName("Should handle null fields in update DTO gracefully")
    void shouldHandleNullFieldsInUpdateDTOGracefully() {
        // Given
        Long eventId = 1L;
        EventDTO updateDTO = new EventDTO();
        // All fields are null in updateDTO
        
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(sampleEvent));
        
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        when(eventRepository.save(eventCaptor.capture())).thenAnswer(val -> val.getArgument(0));

        // When
        Event result = eventService.update(eventId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(eventRepository).findById(eventId);
        verify(eventRepository).save(any(Event.class));
        verify(eventCategoryRepository, never()).findById(anyLong());

        // Verify all fields remain unchanged from the original event
        assertThat(result.getId()).isEqualTo(sampleEvent.getId());
        assertThat(result.getTitle()).isEqualTo(sampleEvent.getTitle());
        assertThat(result.getDescription()).isEqualTo(sampleEvent.getDescription());
        assertThat(result.getLocation()).isEqualTo(sampleEvent.getLocation());
        assertThat(result.getEventDate()).isEqualTo(sampleEvent.getEventDate());
        assertThat(result.getCapacity()).isEqualTo(sampleEvent.getCapacity());
        assertThat(result.getPrice()).isEqualTo(sampleEvent.getPrice());
        assertThat(result.isActive()).isEqualTo(sampleEvent.isActive());
        assertThat(result.getCategory()).isEqualTo(sampleEvent.getCategory());
        assertThat(result.getCreatedAt()).isEqualTo(sampleEvent.getCreatedAt());
    }
}

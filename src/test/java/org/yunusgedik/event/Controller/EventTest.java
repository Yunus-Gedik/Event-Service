package org.yunusgedik.event.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yunusgedik.event.Controller.Event.EventController;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventTest {

    @MockitoBean
    EventService eventService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /event/all success")
    void shouldGetAllEvents() throws Exception {
        Event event = createSampleEvent(null, null);

        when(eventService.getAll()).thenReturn(List.of(event));

        mockMvc.perform(get("/event/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))
            .andExpect(jsonPath("$[0].id").value(100L));
    }

    @Test
    @DisplayName("GET /event/id success")
    void shouldGetEventById() throws Exception {
        Event event = createSampleEvent(100L, "TestName");

        when(eventService.get(100L)).thenReturn(event);

        mockMvc.perform(get("/event/100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.title").value("TestName"));
    }

    @Test
    @DisplayName("POST /event/new success")
    void shouldCreateNewEvent() throws Exception {
        Event event = createSampleEvent(null, null);
        EventDTO eventDTO = createSampleEventDTO();

        when(eventService.create(any(EventDTO.class))).thenReturn(event);

        mockMvc.perform(post("/event/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.title").value("Jazz Night"));
    }

    @Test
    @DisplayName("PATCH /event/update")
    void shouldUpdateEvent() throws Exception {
        Event event = createSampleEvent(null, null);
        EventDTO eventDTO = createSampleEventDTO();

        when(eventService.update(anyLong(), any(EventDTO.class))).thenReturn(event);

        mockMvc.perform(patch("/event/update/1L")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.title").value("Jazz Night"));

    }

    @Test
    @DisplayName("DELETE /event/delete")
    void shouldDeleteEvent() throws Exception {
        Event event = createSampleEvent(null, null);

        when(eventService.delete(100L)).thenReturn(event);

        mockMvc.perform(delete("/event")
                .param("id", String.valueOf(100L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.title").value("Jazz Night"));

        verify(eventService).delete(anyLong());
    }

    private Event createSampleEvent(Long id, String title) {
        EventCategory category = new EventCategory();
        category.setId(1L);
        category.setName("Music");

        Event event = new Event();
        event.setId(id == null ? 100L : id);
        event.setCategory(category);
        event.setTitle(title == null ? "Jazz Night" : title);
        event.setDescription("An evening of smooth jazz performances.");
        event.setLocation("Downtown Concert Hall");
        event.setEventDate(LocalDateTime.of(2025, 8, 15, 20, 0));
        event.setCreatedAt(LocalDateTime.now());
        event.setLastUpdatedAt(LocalDateTime.now());
        event.setCapacity(150);
        event.setPrice(49.99);
        event.setActive(true);

        return event;
    }

    private EventDTO createSampleEventDTO() {
        EventDTO dto = new EventDTO();
        dto.setId(100L);
        dto.setCategoryId(1L);
        dto.setTitle("Jazz Night");
        dto.setDescription("An evening of smooth jazz performances.");
        dto.setLocation("Downtown Concert Hall");
        dto.setEventDate(LocalDateTime.of(2025, 8, 15, 20, 0));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setLastUpdatedAt(LocalDateTime.now());
        dto.setCapacity(150);
        dto.setPrice(49.99);
        dto.setActive(true);
        return dto;
    }
}

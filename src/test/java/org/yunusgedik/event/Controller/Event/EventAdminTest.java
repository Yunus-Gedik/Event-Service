package org.yunusgedik.event.Controller.Event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Security.JwtAuthenticationFilter;
import org.yunusgedik.event.Security.JwtPublicKeyProvider;
import org.yunusgedik.event.Security.JwtValidationService;
import org.yunusgedik.event.Security.SecurityConfig;
import org.yunusgedik.event.Service.EventService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.yunusgedik.event.Controller.Event.MockEventDataCreater.createSampleEvent;
import static org.yunusgedik.event.Controller.Event.MockEventDataCreater.createSampleEventDTO;


@WebMvcTest(controllers = EventAdminController.class)
@Import({JwtAuthenticationFilter.class, JwtValidationService.class, JwtPublicKeyProvider.class, SecurityConfig.class})
public class EventAdminTest {

    @MockitoBean
    EventService eventService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("POST /admin/event/new success")
    void shouldCreateNewEvent() throws Exception {
        Event event = createSampleEvent(null, null);
        EventDTO eventDTO = createSampleEventDTO();

        when(eventService.create(any(EventDTO.class))).thenReturn(event);

        mockMvc.perform(post("/admin/event/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.title").value("Jazz Night"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("PATCH /admin/event/update")
    void shouldUpdateEvent() throws Exception {
        Event event = createSampleEvent(null, null);
        EventDTO eventDTO = createSampleEventDTO();

        when(eventService.update(anyLong(), any(EventDTO.class))).thenReturn(event);

        mockMvc.perform(patch("/admin/event/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.title").value("Jazz Night"));

    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("DELETE /admin/event")
    void shouldDeleteEvent() throws Exception {
        Event event = createSampleEvent(null, null);

        when(eventService.delete(100L)).thenReturn(event);

        mockMvc.perform(delete("/admin/event")
                .param("id", String.valueOf(100L)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.title").value("Jazz Night"));

        verify(eventService).delete(anyLong());
    }
}

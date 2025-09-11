package org.yunusgedik.event.Controller.Event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Security.JwtAuthenticationFilter;
import org.yunusgedik.event.Security.JwtPublicKeyProvider;
import org.yunusgedik.event.Security.JwtValidationService;
import org.yunusgedik.event.Security.SecurityConfig;
import org.yunusgedik.event.Service.EventService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.yunusgedik.event.Helper.MockDataCreator.createSampleEvent;

@WebMvcTest(EventController.class)
@Import({JwtAuthenticationFilter.class, JwtValidationService.class, JwtPublicKeyProvider.class, SecurityConfig.class})
public class EventTest {

    @MockitoBean
    EventService eventService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET /event/all")
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
    void shouldGetEventByIdSuccess() throws Exception {
        Event event = createSampleEvent(100L, "TestName");

        when(eventService.get(100L)).thenReturn(event);

        mockMvc.perform(get("/event/100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.title").value("TestName"));
    }

    @Test
    @DisplayName("GET /event/id fail")
    void shouldGetEventByIdFail() throws Exception {

        when(eventService.get(anyLong()))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        mockMvc.perform(get("/event/100"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /event request param id success")
    void shouldGetEventByIdRequestParam() throws Exception {
        Event event = createSampleEvent(100L, "TestName");

        when(eventService.get(100L)).thenReturn(event);

        mockMvc.perform(get("/event")
                .param("id", "100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.title").value("TestName"));
    }

    @Test
    @DisplayName("GET /event request param id success")
    void shouldGetEventByIdRequestParamFail() throws Exception {

        when(eventService.get(anyLong()))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        mockMvc.perform(get("/event")
                .param("id", "100"))
            .andExpect(status().isNotFound());
    }
}

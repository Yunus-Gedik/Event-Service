package org.yunusgedik.event.Controller.EventCategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Service.EventCategoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventCategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventCategoryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventCategoryService categoryService;

    @Test
    @DisplayName("GET /event-category/all - success")
    void shouldReturnListOfCategories() throws Exception {
        List<EventCategory> categories = List.of(
            new EventCategory(1L, "Tech", new HashSet<>()),
            new EventCategory(2L, "Music", new HashSet<>())
        );
        when(categoryService.getAll()).thenReturn(categories);

        mockMvc.perform(get("/event-category/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].name").value("Tech"));
    }

    @Test
    @DisplayName("GET /event-category/1 success")
    void shouldGetCategoryById() throws Exception {
        EventCategory saved = new EventCategory(1L, "Tech", Set.of());

        when(categoryService.get(1L)).thenReturn(saved);

        mockMvc.perform(get("/event-category/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    @DisplayName("GET /event-category/1 fail")
    void shouldGetCategoryByIdFail() throws Exception {
        when(categoryService.get(anyLong()))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Event Category not found"));

        mockMvc.perform(get("/event-category/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /event-category success")
    void shouldGetCategoryByIdRequestParam() throws Exception {
        EventCategory saved = new EventCategory(1L, "Tech", Set.of());

        when(categoryService.get(1L)).thenReturn(saved);

        mockMvc.perform(get("/event-category")
                .param("id", String.valueOf(1L))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    @DisplayName("GET /event-category fail")
    void shouldGetCategoryByIdRequestParamFail() throws Exception {
        when(categoryService.get(anyLong()))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Event Category not found"));

        mockMvc.perform(get("/event-category")
                .param("id", String.valueOf(1L))
            )
            .andExpect(status().isNotFound());
    }

}

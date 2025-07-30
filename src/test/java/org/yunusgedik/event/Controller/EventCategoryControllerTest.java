package org.yunusgedik.event.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Service.EventCategoryService;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventCategoryController.class)
public class EventCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventCategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /categories - success")
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
    @DisplayName("POST /categories - success")
    void shouldCreateCategory() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO(null, "Business");
        EventCategory saved = new EventCategory(3L, "Business", new HashSet<>());

        when(categoryService.create(any(EventCategoryDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/event-category/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Business"));
    }
}

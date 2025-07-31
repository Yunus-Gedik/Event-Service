package org.yunusgedik.event.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Service.EventCategoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @DisplayName("POST /event-category/new - success")
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
    @DisplayName("PATCH /event-category/update success")
    void shouldUpdateCategory() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO(null, "BeforeUpdate");
        EventCategory updated = new EventCategory(3L, "Business", Set.of());

        when(categoryService.update(any(EventCategoryDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/event-category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Business"));
    }

    @Test
    @DisplayName("DELETE /event-category/1 success")
    void shouldDeleteCategory() throws Exception {
        EventCategory saved = new EventCategory(1L, "Tech", Set.of());

        when(categoryService.delete(1L)).thenReturn(saved);

        mockMvc.perform(delete("/event-category/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    @DisplayName("DELETE /event-category/1 failure")
    void shouldDeleteCategoryFailure() throws Exception {
        when(categoryService.delete(1L)).thenReturn(null);

        mockMvc.perform(delete("/event-category/1"))
            .andExpect(status().isNotFound())
            .andExpect(bodyIsNull());
    }

    private ResultMatcher bodyIsNull() {
        return result -> assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}

package org.yunusgedik.event.Controller.EventCategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Security.JwtAuthenticationFilter;
import org.yunusgedik.event.Security.JwtPublicKeyProvider;
import org.yunusgedik.event.Security.JwtValidationService;
import org.yunusgedik.event.Security.SecurityConfig;
import org.yunusgedik.event.Service.EventCategoryService;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventCategoryAdminController.class)
@Import({JwtAuthenticationFilter.class, JwtValidationService.class, JwtPublicKeyProvider.class, SecurityConfig.class})
public class EventCategoryAdminTest {

    @MockitoBean
    EventCategoryService categoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("POST /event-category/admin/new - success")
    void shouldCreateCategory() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO("Business");
        EventCategory saved = new EventCategory(3L, "Business", new HashSet<>());

        when(categoryService.create(any(EventCategoryDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/admin/event-category/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(saved.getId()))
            .andExpect(jsonPath("$.name").value("Business"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("PATCH /event-category/admin/update success")
    void shouldUpdateCategory() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO("BeforeUpdate");
        EventCategory updated = new EventCategory(3L, "Business", Set.of());

        when(categoryService.update(anyLong(), any(EventCategoryDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/admin/event-category/update/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Business"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("DELETE /event-category/admin/1 success")
    void shouldDeleteCategory() throws Exception {
        EventCategory deleted = new EventCategory(1L, "Tech", Set.of());

        when(categoryService.delete(1L)).thenReturn(deleted);

        mockMvc.perform(delete("/admin/event-category/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(deleted.getId()))
            .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("DELETE /event-category/admin/1 failure")
    void shouldDeleteCategoryFailure() throws Exception {
        when(categoryService.delete(1L)).thenThrow(
            new ResponseStatusException(HttpStatus.NOT_FOUND, "EventCategory not found")
        );

        mockMvc.perform(delete("/admin/event-category/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").value("EventCategory not found"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /event-category/admin/new - unauthorized")
    void shouldNotAllowUnauthenticatedAccess() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO("Business");

        mockMvc.perform(post("/admin/event-category/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"})
    @DisplayName("POST /event-category/admin/new - forbidden for non-admin")
    void shouldNotAllowNonAdminAccess() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO("Business");

        mockMvc.perform(post("/admin/event-category/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("POST /event-category/admin/new - validation failure")
    void shouldValidateInput() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO("");  // Empty name should fail validation

        mockMvc.perform(post("/admin/event-category/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("PATCH /event-category/admin/update - not found")
    void shouldHandleUpdateNonExistentCategory() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO("UpdatedName");

        when(categoryService.update(anyLong(), any(EventCategoryDTO.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        mockMvc.perform(patch("/admin/event-category/update/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").value("Category not found"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("PATCH /event-category/admin/update - invalid input")
    void shouldValidateUpdateInput() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO(null);  // Null name should fail validation

        mockMvc.perform(patch("/admin/event-category/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").exists());
    }
}

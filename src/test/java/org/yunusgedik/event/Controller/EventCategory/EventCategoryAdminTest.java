package org.yunusgedik.event.Controller.EventCategory;

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
import org.springframework.test.web.servlet.ResultMatcher;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Security.JwtAuthenticationFilter;
import org.yunusgedik.event.Security.JwtPublicKeyProvider;
import org.yunusgedik.event.Security.JwtValidationService;
import org.yunusgedik.event.Security.SecurityConfig;
import org.yunusgedik.event.Service.EventCategoryService;
import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        EventCategoryDTO input = new EventCategoryDTO(null, "Business");
        EventCategory saved = new EventCategory(3L, "Business", new HashSet<>());

        when(categoryService.create(any(EventCategoryDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/event-category/admin/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Business"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("PATCH /event-category/admin/update success")
    void shouldUpdateCategory() throws Exception {
        EventCategoryDTO input = new EventCategoryDTO(null, "BeforeUpdate");
        EventCategory updated = new EventCategory(3L, "Business", Set.of());

        when(categoryService.update(any(EventCategoryDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/event-category/admin/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Business"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("DELETE /event-category/admin/1 success")
    void shouldDeleteCategory() throws Exception {
        EventCategory saved = new EventCategory(1L, "Tech", Set.of());

        when(categoryService.delete(1L)).thenReturn(saved);

        mockMvc.perform(delete("/event-category/admin/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Tech"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    @DisplayName("DELETE /event-category/admin/1 failure")
    void shouldDeleteCategoryFailure() throws Exception {
        when(categoryService.delete(1L)).thenReturn(null);

        mockMvc.perform(delete("/event-category/admin/1"))
            .andExpect(status().isNotFound())
            .andExpect(bodyIsNull());
    }

    private ResultMatcher bodyIsNull() {
        return result -> assertThat(result.getResponse().getContentAsString()).isEmpty();
    }
}

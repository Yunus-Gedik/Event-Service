package org.yunusgedik.event.Service.EventCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;
import org.yunusgedik.event.Repository.EventCategoryRepository;
import org.yunusgedik.event.Service.EventCategoryService;
import org.yunusgedik.event.Helper.MockDataCreator;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventCategoryServiceTest {

    @Mock
    private EventCategoryRepository repository;

    @InjectMocks
    private EventCategoryService eventCategoryService;

    private EventCategory sampleCategory;
    private EventCategoryDTO sampleCategoryDTO;

    @BeforeEach
    void setUp() {
        sampleCategory = MockDataCreator.createSampleEventCategory(1L, "Music");
        sampleCategoryDTO = MockDataCreator.createEventCategoryDTO("Music");
    }

    @Test
    @DisplayName("Should get event category by id successfully")
    void shouldGetEventCategoryByIdSuccessfully() {
        // Given
        Long categoryId = 1L;
        when(repository.findById(categoryId)).thenReturn(Optional.of(sampleCategory));

        // When
        EventCategory result = eventCategoryService.get(categoryId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getName()).isEqualTo(sampleCategory.getName());
        verify(repository).findById(categoryId);
    }

    @Test
    @DisplayName("Should throw exception when event category not found by id")
    void shouldThrowExceptionWhenEventCategoryNotFoundById() {
        // Given
        Long categoryId = 999L;
        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventCategoryService.get(categoryId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("EventCategory not found");
        verify(repository).findById(categoryId);
    }

    @Test
    @DisplayName("Should get all event categories successfully")
    void shouldGetAllEventCategoriesSuccessfully() {
        // Given
        List<EventCategory> categories = MockDataCreator.createSampleEventCategoryList();
        when(repository.findAll()).thenReturn(categories);

        // When
        List<EventCategory> result = eventCategoryService.getAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(categories);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should create event category successfully")
    void shouldCreateEventCategorySuccessfully() {
         // When
        ArgumentCaptor<EventCategory> categoryCaptor = ArgumentCaptor.forClass(EventCategory.class);
        when(repository.save(categoryCaptor.capture())).thenAnswer(val -> val.getArgument(0));

        EventCategory result = eventCategoryService.create(sampleCategoryDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(sampleCategoryDTO.getName());
        verify(repository).save(any(EventCategory.class));
    }

    @Test
    @DisplayName("Should update event category successfully")
    void shouldUpdateEventCategorySuccessfully() {
        // Given
        Long categoryId = 1L;
        EventCategoryDTO updateDTO = MockDataCreator.createEventCategoryDTO("Updated Music Category");

        when(repository.findById(categoryId)).thenReturn(Optional.of(sampleCategory));

        ArgumentCaptor<EventCategory> categoryCaptor = ArgumentCaptor.forClass(EventCategory.class);
        when(repository.save(categoryCaptor.capture())).thenAnswer(val -> val.getArgument(0));

        // When
        EventCategory savedCategory = eventCategoryService.update(categoryId, updateDTO);

        // Then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(sampleCategory.getId());
        assertThat(savedCategory.getName()).isEqualTo(updateDTO.getName());
        verify(repository).findById(categoryId);
        verify(repository).save(any(EventCategory.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent event category")
    void shouldThrowExceptionWhenUpdatingNonExistentEventCategory() {
        // Given
        Long categoryId = 999L;
        EventCategoryDTO updateDTO = MockDataCreator.createEventCategoryDTO("Updated Music Category");
        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventCategoryService.update(categoryId, updateDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("EventCategory not found");
        verify(repository).findById(categoryId);
        verify(repository, never()).save(any(EventCategory.class));
    }

    @Test
    @DisplayName("Should delete event category successfully")
    void shouldDeleteEventCategorySuccessfully() {
        // Given
        Long categoryId = 1L;
        when(repository.findById(categoryId)).thenReturn(Optional.of(sampleCategory));

        // When
        EventCategory result = eventCategoryService.delete(categoryId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getName()).isEqualTo(sampleCategory.getName());
        verify(repository).findById(categoryId);
        verify(repository).delete(sampleCategory);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent event category")
    void shouldThrowExceptionWhenDeletingNonExistentEventCategory() {
        // Given
        Long categoryId = 999L;
        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventCategoryService.delete(categoryId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("EventCategory not found");
        verify(repository).findById(categoryId);
        verify(repository, never()).delete(any(EventCategory.class));
    }

    @Test
    @DisplayName("Should handle empty category list")
    void shouldHandleEmptyCategoryList() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<EventCategory> result = eventCategoryService.getAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(repository).findAll();
    }
}

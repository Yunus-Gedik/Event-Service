package org.yunusgedik.event.Helper;

import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Model.EventCategory.EventCategory;
import org.yunusgedik.event.Model.EventCategory.EventCategoryDTO;

import java.time.LocalDateTime;
import java.util.List;

public class MockDataCreator {
    
    // Event-related methods
    public static Event createSampleEvent(Long id, String title) {
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

    public static EventDTO createSampleEventDTO() {
        EventDTO dto = new EventDTO();
        dto.setCategoryId(1L);
        dto.setTitle("Jazz Night");
        dto.setDescription("An evening of smooth jazz performances.");
        dto.setLocation("Downtown Concert Hall");
        dto.setEventDate(LocalDateTime.of(2025, 8, 15, 20, 0));
        dto.setCapacity(150);
        dto.setPrice(49.99);
        dto.setActive(true);
        return dto;
    }

    public static EventDTO createUpdateEventDTO() {
        EventDTO dto = new EventDTO();
        dto.setTitle("Updated Jazz Night");
        dto.setDescription("Updated description");
        dto.setLocation("Updated Location");
        dto.setCapacity(200);
        dto.setPrice(59.99);
        dto.setActive(false);
        return dto;
    }

    public static EventDTO createPartialUpdateEventDTO() {
        EventDTO dto = new EventDTO();
        dto.setTitle("Partially Updated Title");
        dto.setCapacity(175);
        return dto;
    }

    public static List<Event> createSampleEventList() {
        Event event1 = createSampleEvent(1L, "Jazz Night");
        Event event2 = createSampleEvent(2L, "Rock Concert");
        Event event3 = createSampleEvent(3L, "Classical Music");
        return List.of(event1, event2, event3);
    }

    // EventCategory-related methods
    public static EventCategory createSampleEventCategory(Long id, String name) {
        EventCategory category = new EventCategory();
        category.setId(id == null ? 1L : id);
        category.setName(name == null ? "Music" : name);
        return category;
    }

    public static EventCategoryDTO createEventCategoryDTO(String name) {
        EventCategoryDTO dto = new EventCategoryDTO();
        dto.setName(name == null ? "Music" : name);
        return dto;
    }

    public static List<EventCategory> createSampleEventCategoryList() {
        EventCategory category1 = createSampleEventCategory(1L, "Music");
        EventCategory category2 = createSampleEventCategory(2L, "Sports");
        EventCategory category3 = createSampleEventCategory(3L, "Technology");
        return List.of(category1, category2, category3);
    }
}

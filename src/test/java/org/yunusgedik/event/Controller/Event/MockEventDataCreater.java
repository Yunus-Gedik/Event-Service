package org.yunusgedik.event.Controller.Event;

import org.yunusgedik.event.Model.Event.Event;
import org.yunusgedik.event.Model.Event.EventDTO;
import org.yunusgedik.event.Model.EventCategory.EventCategory;

import java.time.LocalDateTime;

public class MockEventDataCreater {
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

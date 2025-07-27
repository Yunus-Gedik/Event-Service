package org.yunusgedik.event.Model.Event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private Long categoryId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDate;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private int capacity;
    private double price;
    private boolean isActive;
}
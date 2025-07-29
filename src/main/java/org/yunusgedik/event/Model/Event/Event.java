package org.yunusgedik.event.Model.Event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yunusgedik.event.Model.EventCategory.EventCategory;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "categoryId")
    private EventCategory category;

    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDate;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private int capacity;
    private double price;
    private boolean active;
}

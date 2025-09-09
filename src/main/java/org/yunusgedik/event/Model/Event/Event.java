package org.yunusgedik.event.Model.Event;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
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
    @JsonManagedReference
    @NotNull
    private EventCategory category;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String location;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime lastUpdatedAt;

    @NotNull
    @Positive
    @Check(constraints = "capacity > 0")
    private int capacity;

    @NotNull
    @PositiveOrZero
    @Check(constraints = "price >= 0")
    private double price;

    @NotNull
    private boolean active;

    @PrePersist
    private void setDataBeforeCreate() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = createdAt;
    }

    @PreUpdate
    private void setDataBeforeUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}

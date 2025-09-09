package org.yunusgedik.event.Model.Event;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    @NotNull(message = "Category ID is required", groups = CreateValidation.class)
    @Min(value = 1, message = "Category ID must be greater than 0")
    private Long categoryId;

    @NotBlank(message = "Title is required", groups = CreateValidation.class)
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required", groups = CreateValidation.class)
    @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required", groups = CreateValidation.class)
    @Size(min = 1, max = 255, message = "Location must be between 1 and 255 characters")
    private String location;

    @NotNull(message = "Event date is required", groups = CreateValidation.class)
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    @NotNull(groups = CreateValidation.class)
    @Positive(message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Price is required", groups = CreateValidation.class)
    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    @NotNull(message = "Active information is required", groups = CreateValidation.class)
    private Boolean active;
}



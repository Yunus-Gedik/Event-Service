package org.yunusgedik.event.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yunusgedik.event.Model.EventCategory.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}

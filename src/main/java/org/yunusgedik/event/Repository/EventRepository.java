package org.yunusgedik.event.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yunusgedik.event.Model.Event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}

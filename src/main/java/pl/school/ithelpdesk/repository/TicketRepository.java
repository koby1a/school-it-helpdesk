package pl.school.ithelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.school.ithelpdesk.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
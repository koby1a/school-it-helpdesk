package pl.school.ithelpdesk.strategy;

import org.springframework.stereotype.Component;
import pl.school.ithelpdesk.entity.TicketPriority;
import org.springframework.core.annotation.Order;

@Component
@Order(1)
public class HighPriorityStrategy implements TicketPriorityStrategy {

    @Override
    public boolean supports(String title, String description) {
        String text = (title + " " + description).toLowerCase();

        return text.contains("nie działa")
                || text.contains("awaria")
                || text.contains("pilne")
                || text.contains("nie uruchamia")
                || text.contains("brak internetu");
    }

    @Override
    public TicketPriority getPriority() {
        return TicketPriority.HIGH;
    }
}
package pl.school.ithelpdesk.strategy;

import org.springframework.stereotype.Component;
import pl.school.ithelpdesk.entity.TicketPriority;
import org.springframework.core.annotation.Order;

@Component
@Order(2)
public class MediumPriorityStrategy implements TicketPriorityStrategy {

    @Override
    public boolean supports(String title, String description) {
        String text = (title + " " + description).toLowerCase();

        return text.contains("drukarka")
                || text.contains("komputer")
                || text.contains("projektor")
                || text.contains("teams")
                || text.contains("konto");
    }

    @Override
    public TicketPriority getPriority() {
        return TicketPriority.MEDIUM;
    }
}
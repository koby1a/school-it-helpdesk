package pl.school.ithelpdesk.strategy;

import org.springframework.stereotype.Component;
import pl.school.ithelpdesk.entity.TicketPriority;
import org.springframework.core.annotation.Order;

@Component
@Order(3)
public class LowPriorityStrategy implements TicketPriorityStrategy {

    @Override
    public boolean supports(String title, String description) {
        return true;
    }

    @Override
    public TicketPriority getPriority() {
        return TicketPriority.LOW;
    }
}
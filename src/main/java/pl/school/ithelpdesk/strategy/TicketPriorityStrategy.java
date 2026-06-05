package pl.school.ithelpdesk.strategy;

import pl.school.ithelpdesk.entity.TicketPriority;

public interface TicketPriorityStrategy {

    boolean supports(String title, String description);

    TicketPriority getPriority();
}
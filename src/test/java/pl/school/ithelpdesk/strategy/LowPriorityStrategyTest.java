package pl.school.ithelpdesk.strategy;

import org.junit.jupiter.api.Test;
import pl.school.ithelpdesk.entity.TicketPriority;

import static org.junit.jupiter.api.Assertions.*;

class LowPriorityStrategyTest {

    private final LowPriorityStrategy strategy = new LowPriorityStrategy();

    @Test
    void shouldSupportEveryTicket() {
        boolean result = strategy.supports(
                "Dowolny temat",
                "Dowolny opis zgłoszenia"
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnLowPriority() {
        TicketPriority priority = strategy.getPriority();

        assertEquals(TicketPriority.LOW, priority);
    }
}
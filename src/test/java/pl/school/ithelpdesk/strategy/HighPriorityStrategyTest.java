package pl.school.ithelpdesk.strategy;

import org.junit.jupiter.api.Test;
import pl.school.ithelpdesk.entity.TicketPriority;

import static org.junit.jupiter.api.Assertions.*;

class HighPriorityStrategyTest {

    private final HighPriorityStrategy strategy = new HighPriorityStrategy();

    @Test
    void shouldSupportHighPriorityTicketWhenTextContainsPilne() {
        boolean result = strategy.supports(
                "Pilne zgłoszenie",
                "Komputer w sekretariacie nie działa"
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnHighPriority() {
        TicketPriority priority = strategy.getPriority();

        assertEquals(TicketPriority.HIGH, priority);
    }

    @Test
    void shouldNotSupportNormalTicket() {
        boolean result = strategy.supports(
                "Pytanie o ustawienia",
                "Chciałbym zmienić tapetę"
        );

        assertFalse(result);
    }
}
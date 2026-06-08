package pl.school.ithelpdesk.strategy;

import org.junit.jupiter.api.Test;
import pl.school.ithelpdesk.entity.TicketPriority;

import static org.junit.jupiter.api.Assertions.*;

class MediumPriorityStrategyTest {

    private final MediumPriorityStrategy strategy = new MediumPriorityStrategy();

    @Test
    void shouldSupportMediumPriorityTicketWhenTextContainsPrinter() {
        boolean result = strategy.supports(
                "Problem z drukarką",
                "Drukarka w pokoju nauczycielskim drukuje bardzo wolno"
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnMediumPriority() {
        TicketPriority priority = strategy.getPriority();

        assertEquals(TicketPriority.MEDIUM, priority);
    }

    @Test
    void shouldNotSupportLowPriorityTicket() {
        boolean result = strategy.supports(
                "Pytanie o tapetę",
                "Chciałbym zmienić tapetę na pulpicie"
        );

        assertFalse(result);
    }
}
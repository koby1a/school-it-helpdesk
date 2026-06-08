package pl.school.ithelpdesk.controller;

import org.junit.jupiter.api.Test;
import pl.school.ithelpdesk.dto.AssignTicketRequest;
import pl.school.ithelpdesk.dto.CreateTicketRequest;
import pl.school.ithelpdesk.dto.TicketResponse;
import pl.school.ithelpdesk.entity.TicketStatus;
import pl.school.ithelpdesk.service.TicketService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    private final TicketService ticketService = mock(TicketService.class);
    private final TicketController ticketController = new TicketController(ticketService);

    @Test
    void shouldCreateTicket() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Problem z drukarką");
        request.setDescription("Drukarka nie drukuje");

        TicketResponse expectedResponse = new TicketResponse(
                1L,
                "Problem z drukarką",
                "OPEN",
                "MEDIUM",
                null
        );

        when(ticketService.createTicket(request)).thenReturn(expectedResponse);

        TicketResponse response = ticketController.createTicket(request);

        assertEquals(1L, response.getId());
        assertEquals("Problem z drukarką", response.getTitle());
        assertEquals("OPEN", response.getStatus());
        assertEquals("MEDIUM", response.getPriority());
        assertNull(response.getAssignedUsername());

        verify(ticketService).createTicket(request);
    }

    @Test
    void shouldReturnAllTickets() {
        TicketResponse ticketResponse = new TicketResponse(
                1L,
                "Nie działa internet",
                "OPEN",
                "HIGH",
                "Death"
        );

        when(ticketService.getAllTickets()).thenReturn(List.of(ticketResponse));

        List<TicketResponse> responses = ticketController.getAllTickets();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Nie działa internet", responses.get(0).getTitle());
        assertEquals("OPEN", responses.get(0).getStatus());
        assertEquals("HIGH", responses.get(0).getPriority());
        assertEquals("Death", responses.get(0).getAssignedUsername());

        verify(ticketService).getAllTickets();
    }

    @Test
    void shouldUpdateTicketStatus() {
        TicketResponse expectedResponse = new TicketResponse(
                1L,
                "Problem z komputerem",
                "RESOLVED",
                "MEDIUM",
                null
        );

        when(ticketService.updateTicketStatus(1L, TicketStatus.RESOLVED))
                .thenReturn(expectedResponse);

        TicketResponse response = ticketController.updateTicketStatus(
                1L,
                TicketStatus.RESOLVED
        );

        assertEquals(1L, response.getId());
        assertEquals("RESOLVED", response.getStatus());

        verify(ticketService).updateTicketStatus(1L, TicketStatus.RESOLVED);
    }

    @Test
    void shouldAssignTicketToUser() {
        AssignTicketRequest request = new AssignTicketRequest();
        request.setUserId(5L);

        TicketResponse expectedResponse = new TicketResponse(
                1L,
                "Nie działa drukarka",
                "IN_PROGRESS",
                "MEDIUM",
                "Death"
        );

        when(ticketService.assignTicketToUser(1L, request))
                .thenReturn(expectedResponse);

        TicketResponse response = ticketController.assignTicketToUser(1L, request);

        assertEquals(1L, response.getId());
        assertEquals("Death", response.getAssignedUsername());

        verify(ticketService).assignTicketToUser(1L, request);
    }
}
package pl.school.ithelpdesk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.school.ithelpdesk.dto.AssignTicketRequest;
import pl.school.ithelpdesk.dto.CreateTicketRequest;
import pl.school.ithelpdesk.dto.TicketResponse;
import pl.school.ithelpdesk.entity.Role;
import pl.school.ithelpdesk.entity.Ticket;
import pl.school.ithelpdesk.entity.TicketPriority;
import pl.school.ithelpdesk.entity.TicketStatus;
import pl.school.ithelpdesk.entity.User;
import pl.school.ithelpdesk.exception.TicketNotFoundException;
import pl.school.ithelpdesk.exception.UserNotFoundException;
import pl.school.ithelpdesk.repository.TicketRepository;
import pl.school.ithelpdesk.repository.UserRepository;
import pl.school.ithelpdesk.strategy.HighPriorityStrategy;
import pl.school.ithelpdesk.strategy.LowPriorityStrategy;
import pl.school.ithelpdesk.strategy.MediumPriorityStrategy;
import pl.school.ithelpdesk.strategy.TicketPriorityStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private UserRepository userRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        userRepository = mock(UserRepository.class);

        List<TicketPriorityStrategy> strategies = List.of(
                new HighPriorityStrategy(),
                new MediumPriorityStrategy(),
                new LowPriorityStrategy()
        );

        ticketService = new TicketService(
                ticketRepository,
                userRepository,
                strategies
        );
    }

    @Test
    void shouldCreateTicketWithOpenStatusAndHighPriority() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Pilne - brak internetu");
        request.setDescription("W sekretariacie nie działa internet");

        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket ticket = invocation.getArgument(0);
            ticket.setId(1L);
            return ticket;
        });

        TicketResponse response = ticketService.createTicket(request);

        assertEquals(1L, response.getId());
        assertEquals("Pilne - brak internetu", response.getTitle());
        assertEquals("OPEN", response.getStatus());
        assertEquals("HIGH", response.getPriority());
        assertNull(response.getAssignedUsername());

        verify(ticketRepository).save(argThat(ticket ->
                ticket.getTitle().equals("Pilne - brak internetu")
                        && ticket.getDescription().equals("W sekretariacie nie działa internet")
                        && ticket.getStatus() == TicketStatus.OPEN
                        && ticket.getPriority() == TicketPriority.HIGH
                        && ticket.getCreatedAt() != null
        ));
    }

    @Test
    void shouldReturnAllTickets() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Problem z drukarką");
        ticket.setDescription("Drukarka nie drukuje");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedAt(LocalDateTime.now());

        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        List<TicketResponse> responses = ticketService.getAllTickets();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Problem z drukarką", responses.get(0).getTitle());
        assertEquals("OPEN", responses.get(0).getStatus());
        assertEquals("MEDIUM", responses.get(0).getPriority());
    }

    @Test
    void shouldUpdateTicketStatus() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Problem z komputerem");
        ticket.setDescription("Komputer działa wolno");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedAt(LocalDateTime.now());

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response = ticketService.updateTicketStatus(1L, TicketStatus.RESOLVED);

        assertEquals(1L, response.getId());
        assertEquals("RESOLVED", response.getStatus());

        verify(ticketRepository).save(argThat(savedTicket ->
                savedTicket.getStatus() == TicketStatus.RESOLVED
        ));
    }

    @Test
    void shouldThrowTicketNotFoundExceptionWhenUpdatingMissingTicket() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.updateTicketStatus(999L, TicketStatus.RESOLVED)
        );

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void shouldAssignTicketToUser() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Nie działa drukarka");
        ticket.setDescription("Drukarka nie odpowiada");
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedAt(LocalDateTime.now());

        User user = new User();
        user.setId(5L);
        user.setUsername("Death");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        AssignTicketRequest request = new AssignTicketRequest();
        request.setUserId(5L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response = ticketService.assignTicketToUser(1L, request);

        assertEquals(1L, response.getId());
        assertEquals("Death", response.getAssignedUsername());

        verify(ticketRepository).save(argThat(savedTicket ->
                savedTicket.getAssignedUser() != null
                        && savedTicket.getAssignedUser().getUsername().equals("Death")
        ));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenAssigningMissingUser() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Nie działa drukarka");
        ticket.setDescription("Drukarka nie odpowiada");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedAt(LocalDateTime.now());

        AssignTicketRequest request = new AssignTicketRequest();
        request.setUserId(999L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> ticketService.assignTicketToUser(1L, request)
        );

        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
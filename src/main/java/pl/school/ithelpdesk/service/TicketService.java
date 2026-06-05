package pl.school.ithelpdesk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.school.ithelpdesk.dto.CreateTicketRequest;
import pl.school.ithelpdesk.dto.TicketResponse;
import pl.school.ithelpdesk.dto.AssignTicketRequest;
import pl.school.ithelpdesk.entity.User;
import pl.school.ithelpdesk.entity.Ticket;
import pl.school.ithelpdesk.entity.TicketStatus;
import pl.school.ithelpdesk.exception.TicketNotFoundException;
import pl.school.ithelpdesk.exception.UserNotFoundException;
import pl.school.ithelpdesk.repository.UserRepository;
import pl.school.ithelpdesk.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketResponse createTicket(CreateTicketRequest request) {

        Ticket ticket = new Ticket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        return mapToResponse(savedTicket);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TicketResponse updateTicketStatus(Long id, TicketStatus status) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        ticket.setStatus(status);

        Ticket savedTicket = ticketRepository.save(ticket);

        return mapToResponse(savedTicket);
    }

    public TicketResponse assignTicketToUser(Long ticketId, AssignTicketRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        ticket.setAssignedUser(user);

        Ticket savedTicket = ticketRepository.save(ticket);

        return mapToResponse(savedTicket);
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        String assignedUsername = ticket.getAssignedUser() != null
                ? ticket.getAssignedUser().getUsername()
                : null;

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus().name(),
                assignedUsername
        );
    }
}
package pl.school.ithelpdesk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.school.ithelpdesk.dto.CreateTicketRequest;
import pl.school.ithelpdesk.dto.TicketResponse;
import pl.school.ithelpdesk.entity.Ticket;
import pl.school.ithelpdesk.entity.TicketStatus;
import pl.school.ithelpdesk.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketResponse createTicket(CreateTicketRequest request) {

        Ticket ticket = new Ticket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        return new TicketResponse(
                savedTicket.getId(),
                savedTicket.getTitle(),
                savedTicket.getStatus().name()
        );
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticket -> new TicketResponse(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getStatus().name()
                ))
                .toList();
    }

    public TicketResponse updateTicketStatus(Long id, TicketStatus status) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(status);

        Ticket savedTicket = ticketRepository.save(ticket);

        return new TicketResponse(
                savedTicket.getId(),
                savedTicket.getTitle(),
                savedTicket.getStatus().name()
        );
    }
}
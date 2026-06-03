package pl.school.ithelpdesk.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.school.ithelpdesk.dto.CreateTicketRequest;
import pl.school.ithelpdesk.dto.TicketResponse;
import pl.school.ithelpdesk.service.TicketService;
import pl.school.ithelpdesk.entity.TicketStatus;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public TicketResponse createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        return ticketService.createTicket(request);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status
    ) {
        return ticketService.updateTicketStatus(id, status);
    }
}
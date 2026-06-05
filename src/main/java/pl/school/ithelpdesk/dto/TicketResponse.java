package pl.school.ithelpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private String title;
    private String status;
    private String priority;
    private String assignedUsername;
}
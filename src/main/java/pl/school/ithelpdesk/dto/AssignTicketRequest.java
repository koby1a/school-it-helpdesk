package pl.school.ithelpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTicketRequest {

    @NotNull(message = "User id cannot be null")
    private Long userId;
}
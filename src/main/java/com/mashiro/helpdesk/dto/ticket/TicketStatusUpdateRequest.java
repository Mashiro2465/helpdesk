package com.mashiro.helpdesk.dto.ticket;

import com.mashiro.helpdesk.domain.ticket.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateRequest(
        @NotNull TicketStatus status
) {}
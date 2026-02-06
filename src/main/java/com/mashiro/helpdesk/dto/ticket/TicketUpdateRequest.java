package com.mashiro.helpdesk.dto.ticket;

import com.mashiro.helpdesk.domain.ticket.TicketCategory;
import com.mashiro.helpdesk.domain.ticket.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketUpdateRequest(
        @NotNull TicketCategory category,
        @NotNull TicketPriority priority,
        @NotBlank String title,
        @NotBlank String content
) {}

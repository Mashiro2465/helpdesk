package com.mashiro.helpdesk.dto.ticket;

import jakarta.validation.constraints.NotBlank;

public record TicketAssignRequest(
        @NotBlank String assigneeUsername
) {}

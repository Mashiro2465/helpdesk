package com.mashiro.helpdesk.dto.ticket;

import com.mashiro.helpdesk.domain.ticket.*;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        TicketCategory category,
        TicketStatus status,
        TicketPriority priority,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

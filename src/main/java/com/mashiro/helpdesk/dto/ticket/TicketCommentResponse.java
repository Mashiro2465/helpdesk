package com.mashiro.helpdesk.dto.ticket;

import java.time.LocalDateTime;

public record TicketCommentResponse(
        Long id,
        String authorUsername,
        String content,
        LocalDateTime createdAt
) {}

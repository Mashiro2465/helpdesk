package com.mashiro.helpdesk.service;

import com.mashiro.helpdesk.domain.ticket.Ticket;
import com.mashiro.helpdesk.dto.ticket.TicketCreateRequest;
import com.mashiro.helpdesk.dto.ticket.TicketResponse;
import com.mashiro.helpdesk.dto.ticket.TicketUpdateRequest;
import com.mashiro.helpdesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public Long create(TicketCreateRequest req) {
        Ticket ticket = Ticket.create(req.category(), req.priority(), req.title(), req.content());
        return ticketRepository.save(ticket).getId();
    }

    @Transactional(readOnly = true)
    public TicketResponse get(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found: " + id));

        return new TicketResponse(
                t.getId(), t.getCategory(), t.getStatus(), t.getPriority(),
                t.getTitle(), t.getContent(), t.getCreatedAt(), t.getUpdatedAt()
        );
    }

    public void update(Long id, TicketUpdateRequest req) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found: " + id));

        t.update(req.category(), req.priority(), req.title(), req.content());
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }
}

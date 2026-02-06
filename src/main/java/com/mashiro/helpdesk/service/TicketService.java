package com.mashiro.helpdesk.service;

import com.mashiro.helpdesk.domain.ticket.Ticket;
import com.mashiro.helpdesk.dto.ticket.TicketCreateRequest;
import com.mashiro.helpdesk.dto.ticket.TicketResponse;
import com.mashiro.helpdesk.dto.ticket.TicketUpdateRequest;
import com.mashiro.helpdesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static com.mashiro.helpdesk.repository.spec.TicketSpecs.*;

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


    @Transactional(readOnly = true)
    public Page<TicketResponse> list(Pageable pageable) {
        return ticketRepository.findAll(pageable)
                .map(t -> new TicketResponse(
                        t.getId(), t.getCategory(), t.getStatus(), t.getPriority(),
                        t.getTitle(), t.getContent(), t.getCreatedAt(), t.getUpdatedAt()
                ));
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> search(
            com.mashiro.helpdesk.domain.ticket.TicketStatus status,
            com.mashiro.helpdesk.domain.ticket.TicketCategory category,
            com.mashiro.helpdesk.domain.ticket.TicketPriority priority,
            String q,
            Pageable pageable
    ) {
        Specification<com.mashiro.helpdesk.domain.ticket.Ticket> spec =
                Specification.where(status(status))
                        .and(category(category))
                        .and(priority(priority))
                        .and(keyword(q));

        return ticketRepository.findAll(spec, pageable)
                .map(t -> new TicketResponse(
                        t.getId(), t.getCategory(), t.getStatus(), t.getPriority(),
                        t.getTitle(), t.getContent(), t.getCreatedAt(), t.getUpdatedAt()
                ));
    }

    public void changeStatus(Long id, com.mashiro.helpdesk.domain.ticket.TicketStatus status) {
        com.mashiro.helpdesk.domain.ticket.Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found: " + id));

        t.changeStatus(status);
    }

}

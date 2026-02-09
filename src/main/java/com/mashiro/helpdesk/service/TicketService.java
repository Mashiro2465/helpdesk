package com.mashiro.helpdesk.service;

import com.mashiro.helpdesk.domain.ticket.Ticket;
import com.mashiro.helpdesk.domain.ticket.TicketComment;
import com.mashiro.helpdesk.dto.ticket.TicketCommentResponse;
import com.mashiro.helpdesk.dto.ticket.TicketCreateRequest;
import com.mashiro.helpdesk.dto.ticket.TicketResponse;
import com.mashiro.helpdesk.dto.ticket.TicketUpdateRequest;
import com.mashiro.helpdesk.repository.TicketCommentRepository;
import com.mashiro.helpdesk.repository.TicketRepository;
import com.mashiro.helpdesk.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TicketCommentRepository ticketCommentRepository;


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

    //담당자 지정
    public void assign(Long ticketId, String assigneeUsername) {
        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found: " + ticketId));

        var user = userRepository.findByUsername(assigneeUsername)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + assigneeUsername));

        ticket.assignTo(user);
    }

    //댓글 작성
    public Long addComment(Long ticketId, String authorUsername, String content) {
        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found: " + ticketId));

        var author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + authorUsername));

        var comment = ticketCommentRepository.save(TicketComment.create(ticket, author, content));
        return comment.getId();
    }

    //댓글 목록 조회
    @Transactional(readOnly = true)
    public java.util.List<TicketCommentResponse> getComments(Long ticketId) {
        return ticketCommentRepository.findByTicketIdOrderByIdAsc(ticketId).stream()
                .map(c -> new TicketCommentResponse(
                        c.getId(),
                        c.getAuthor().getUsername(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();
    }


}

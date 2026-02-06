package com.mashiro.helpdesk.repository.spec;

import com.mashiro.helpdesk.domain.ticket.*;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecs {

    public static Specification<Ticket> status(TicketStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Ticket> category(TicketCategory category) {
        return (root, query, cb) -> category == null ? cb.conjunction() : cb.equal(root.get("category"), category);
    }

    public static Specification<Ticket> priority(TicketPriority priority) {
        return (root, query, cb) -> priority == null ? cb.conjunction() : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Ticket> keyword(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return cb.conjunction();
            String like = "%" + q.trim() + "%";
            return cb.or(
                    cb.like(root.get("title"), like),
                    cb.like(root.get("content"), like)
            );
        };
    }
}

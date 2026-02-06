package com.mashiro.helpdesk.repository;

import com.mashiro.helpdesk.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

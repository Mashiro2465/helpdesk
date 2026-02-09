package com.mashiro.helpdesk.domain.ticket;

import com.mashiro.helpdesk.domain.user.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "ticket_comments")
public class TicketComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private AppUser author;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static TicketComment create(Ticket ticket, AppUser author, String content) {
        TicketComment c = new TicketComment();
        c.ticket = ticket;
        c.author = author;
        c.content = content;
        c.createdAt = LocalDateTime.now();
        return c;
    }
}

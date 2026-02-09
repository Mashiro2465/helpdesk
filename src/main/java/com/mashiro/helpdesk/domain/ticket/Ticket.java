package com.mashiro.helpdesk.domain.ticket;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.mashiro.helpdesk.domain.user.AppUser;


import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "tickets")
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private AppUser assignee;


    public static Ticket create(TicketCategory category, TicketPriority priority, String title, String content) {
        Ticket t = new Ticket();
        t.category = category;
        t.priority = priority;
        t.status = TicketStatus.OPEN;
        t.title = title;
        t.content = content;
        t.createdAt = LocalDateTime.now();
        t.updatedAt = LocalDateTime.now();
        return t;
    }

    public void update(TicketCategory category, TicketPriority priority, String title, String content) {
        this.category = category;
        this.priority = priority;
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(TicketStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignTo(AppUser assignee) {
        this.assignee = assignee;
        this.updatedAt = java.time.LocalDateTime.now();
    }

}

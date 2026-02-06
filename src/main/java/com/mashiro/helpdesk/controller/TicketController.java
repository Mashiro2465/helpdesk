package com.mashiro.helpdesk.controller;

import com.mashiro.helpdesk.dto.ticket.TicketCreateRequest;
import com.mashiro.helpdesk.dto.ticket.TicketResponse;
import com.mashiro.helpdesk.dto.ticket.TicketUpdateRequest;
import com.mashiro.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mashiro.helpdesk.domain.ticket.TicketCategory;
import com.mashiro.helpdesk.domain.ticket.TicketPriority;
import com.mashiro.helpdesk.domain.ticket.TicketStatus;
import org.springframework.web.bind.annotation.RequestParam;
import com.mashiro.helpdesk.dto.ticket.TicketStatusUpdateRequest;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody TicketCreateRequest req) {
        Long id = ticketService.create(req);
        return Map.of("id", id);
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable Long id) {
        return ticketService.get(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody TicketUpdateRequest req) {
        ticketService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }

    @GetMapping
    public Page<TicketResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ticketService.list(pageable);
    }

    @GetMapping("/search")
    public Page<TicketResponse> search(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketCategory category,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ticketService.search(status, category, priority, q, pageable);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody TicketStatusUpdateRequest req
    ) {
        ticketService.changeStatus(id, req.status());
    }

}

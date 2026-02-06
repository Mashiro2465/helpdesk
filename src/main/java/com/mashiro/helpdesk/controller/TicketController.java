package com.mashiro.helpdesk.controller;

import com.mashiro.helpdesk.dto.ticket.TicketCreateRequest;
import com.mashiro.helpdesk.dto.ticket.TicketResponse;
import com.mashiro.helpdesk.dto.ticket.TicketUpdateRequest;
import com.mashiro.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}

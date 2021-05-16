package com.adiSuper.desk.controller;

import com.adiSuper.desk.model.Agent;
import com.adiSuper.desk.model.Ticket;
import com.adiSuper.desk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TicketController {

  @Autowired
  private TicketService ticketService;


  @GetMapping("/v1/tickets/{id}")
  public Ticket getTicketById( @PathVariable UUID id){
    return ticketService.getTicketById(id);
  }

  @GetMapping("/v1/tickets")
  public List<Ticket> getTickets(@RequestBody Optional<Ticket> filters){
    return ticketService.getAllTickets(filters);
  }

  @PostMapping("/v1/tickets")
  public UUID postTicket(@RequestBody @Valid Ticket ticket){
    return ticketService.addTicket(ticket);
  }

  @PatchMapping("/v1/tickets/{id}")
  public Ticket patchTicket(@PathVariable UUID id, @RequestBody Ticket ticket){
    return ticketService.editTicket(id, ticket);
  }

  @DeleteMapping("/v1/tickets/{id}")
  public boolean deleteTicketById(@PathVariable UUID id){
    return ticketService.removeTicket(id);
  }
}

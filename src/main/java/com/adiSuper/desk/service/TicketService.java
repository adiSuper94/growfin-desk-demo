package com.adiSuper.desk.service;

import com.adiSuper.desk.dbAccessor.AgentDbAccessor;
import com.adiSuper.desk.dbAccessor.CustomerDbAccessor;
import com.adiSuper.desk.dbAccessor.TicketDbAccessor;
import com.adiSuper.desk.messaging.Exchanges;
import com.adiSuper.desk.messaging.MessageQueueManger;
import com.adiSuper.desk.model.Agent;
import com.adiSuper.desk.model.Customer;
import com.adiSuper.desk.model.Ticket;
import com.adiSuper.generated.core.Tables;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TicketService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TicketDbAccessor ticketDbAccessor;

  @Autowired
  private AgentDbAccessor agentDbAccessor;

  @Autowired
  private CustomerDbAccessor customerDbAccessor;

  @Autowired
  private DSLContext db;

  @Autowired
  private MessageQueueManger messageQueueManger;

  @Autowired
  private TicketAssigningPolicy ticketAssigningPolicy;


  public Ticket getTicketById(UUID id) {
    Ticket ticket = ticketDbAccessor.fetchOne(Tables.TICKET.ID, id);
    this.enrichTicketInfo(ticket);
    return ticket;
  }

  public List<Ticket> getAllTickets(Optional<Ticket> filters) {
    List<Ticket> tickets = ticketDbAccessor.fetchAll(filters);
    for (Ticket ticket : tickets) {
      this.enrichTicketInfo(ticket);
    }
    return tickets;
  }

  public UUID addTicket(Ticket ticket) {
    UUID ticketId = ticketDbAccessor.insertOne(ticket);
    messageQueueManger.queueMessage(Exchanges.OPEN_TICKET, ticketId);
    return ticketId;
  }

  public boolean removeTicket(UUID ticketID) {
    return ticketDbAccessor.deleteOneById(ticketID);
  }

  public Ticket editTicket(UUID ticketId, Ticket ticket) {
    return ticketDbAccessor.updateTicketId(ticketId, ticket);
  }

  public void assignTickets(List<UUID> ticketIds){
    Map<UUID, UUID> ticketVsAgentMap = ticketAssigningPolicy.getAgentsForNextTickets(ticketIds);
    ticketDbAccessor.batchUpdateAssigneeId(ticketVsAgentMap);
  }

  public void assignTicket(UUID ticketId){
    UUID agentId = ticketAssigningPolicy.getNextAgent(ticketId);
    ticketDbAccessor.updateAssigneeId(ticketId, agentId);
  }
  private Ticket enrichTicketInfo(Ticket ticket) {
    try {
      if (ticket.getCreatorId() != null) {
        Agent createdBy = agentDbAccessor.fetchOne(Tables.AGENT.ID, ticket.getCreatorId());
        ticket.setCreatedBy(createdBy);
      }
      if (ticket.getAssigneeId() != null) {
        Agent assignee = agentDbAccessor.fetchOne(Tables.AGENT.ID, ticket.getAssigneeId());
        ticket.setAssignee(assignee);
      }
      if (ticket.getCustomerId() != null) {
        Customer customer = customerDbAccessor.fetchOne(Tables.CUSTOMER.ID, ticket.getCreatorId());
        ticket.setCustomer(customer);
      }
    } catch (Exception e) {
      String logString = String.format("Error while enriching ticket id:: %s (creatorId::%s , assigneeId::%s , customerId::%s)",
          ticket.getId(), ticket.getCreatorId(), ticket.getAssigneeId(), ticket.getCustomerId());
      logger.warn(logString, e);
      return ticket;
    }
    return ticket;
  }
}

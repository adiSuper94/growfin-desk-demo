package com.adiSuper.desk.dbAccessor;


import com.adiSuper.desk.model.Ticket;
import com.adiSuper.generated.core.Tables;
import com.adiSuper.generated.core.enums.TicketHistoryType;
import com.adiSuper.generated.core.enums.TicketStatus;
import com.adiSuper.generated.core.tables.records.TicketHistoryRecord;
import com.adiSuper.generated.core.tables.records.TicketRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdateConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.trueCondition;

@Repository
public class TicketDbAccessor extends BaseDbAccessor<TicketRecord, Ticket, com.adiSuper.generated.core.tables.pojos.Ticket, UUID> {

  @Autowired
  private OrgDbAccessor orgDbAccessor;

  @Autowired
  private DSLContext db;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public List<Ticket> fetchAll(Optional<Ticket> optionalTicket) {
    if (optionalTicket.isPresent()) {
      return this.fetchAndFilter(optionalTicket.get());
    }
    List<Ticket> tickets = fetchWithCondition(trueCondition());
    return tickets;
  }

  public List<Ticket> fetchAndFilter(Ticket ticket) {
    Condition condition = andAllCriteria(ticket);
    List<Ticket> tickets = fetchWithCondition(condition);
    return tickets;
  }

  public Condition andAllCriteria(Ticket ticket) {
    Condition condition = trueCondition();
    if (ticket.getStatus() != null) {
      condition = condition.and(Tables.TICKET.STATUS.eq(ticket.getStatus()));
    }
    if (ticket.getAssigneeId() != null) {
      condition = condition.and(Tables.TICKET.ASSIGNEE_ID.eq(ticket.getCustomerId()));
    }
    if (ticket.getCreatorId() != null) {
      condition = condition.and(Tables.TICKET.CUSTOMER_ID.eq(ticket.getCustomerId()));
    }
    return condition;
  }

  private List<Ticket> fetchWithCondition(Condition condition) {
    List<Ticket> tickets = db.selectFrom(Tables.TICKET).where(condition).fetch().into(Ticket.class);
    if (tickets.isEmpty()) {
      throw new EntityNotFoundException("No Ticket Found :(");
    }
    return tickets;
  }

  public UUID insertOne(Ticket ticket) {
    this.setOrgId(ticket);
    LocalDateTime now = LocalDateTime.now();
    if (ticket.getStatus() == null) {
      ticket.setStatus(TicketStatus.open);
    }
    ticket.setCreatedAt(now);
    ticket.setModifiedAt(now);
    TicketRecord ticketRecord = db.newRecord(Tables.TICKET, ticket);
    UUID ticketId = db.insertInto(Tables.TICKET).set(ticketRecord)
        .returningResult(Tables.TICKET.ID)
        .fetchOne().value1();

    return ticketId;
  }

  public Ticket updateTicketId(UUID ticketId, Ticket ticket) {
    TicketRecord ticketRecord = db.fetchOne(Tables.TICKET, Tables.TICKET.ID.eq(ticketId));
    db.transaction(configuration -> {
      List<TicketHistoryRecord> historyRecords = this.updateTicketRecord(ticketRecord, ticket);
      ticketRecord.store();
      for (TicketHistoryRecord historyRecord : historyRecords) {
        historyRecord.store();
      }
    });
    return ticketRecord.into(Ticket.class);
  }

  public Map<UUID, Integer> getTicketsGroupedByAssigneeId() {
    Map<UUID, Integer> assignedTicketsMap = db.select(Tables.TICKET.ASSIGNEE_ID, count())
        .from(Tables.TICKET)
        .where(Tables.TICKET.STATUS.in(TicketStatus.open, TicketStatus.customer_responded))
        .groupBy(Tables.TICKET.ASSIGNEE_ID)
        .fetchMap(Tables.TICKET.ASSIGNEE_ID, count());
    return assignedTicketsMap;
  }

  public List<UUID> filterOpenTickets(List<UUID> ticketIds) {
    List<UUID> openTickets =db.select(Tables.TICKET.ID).from(Tables.TICKET)
        .where(Tables.TICKET.STATUS.eq(TicketStatus.open).and(Tables.TICKET.ID.in(ticketIds)))
        .fetch(Tables.TICKET.ID);
    return openTickets;
  }

  public void batchUpdateAssigneeId(Map<UUID, UUID> ticketVsAssigneeMap){
    List<UpdateConditionStep<TicketRecord>> updates = new ArrayList<>();
    for (UUID ticketId : ticketVsAssigneeMap.keySet()) {
      updates.add(this.getQueryForAssigneeUpdate(ticketId, ticketVsAssigneeMap.get(ticketId)));
    }
    db.batch(updates).execute();
  }

  public void updateAssigneeId(UUID ticketId, UUID agnetId){
    this.getQueryForAssigneeUpdate(ticketId, agnetId).execute();
  }
  private UpdateConditionStep<TicketRecord> getQueryForAssigneeUpdate(UUID ticketId, UUID agentId){
    return db.update(Tables.TICKET)
        .set(Tables.TICKET.ASSIGNEE_ID, agentId)
        .where(Tables.TICKET.ID.eq(ticketId));
  }
  public boolean isTicketOpen(UUID ticketId){
    List<UUID> openTickets = this.filterOpenTickets(Collections.singletonList(ticketId));
    if(openTickets.size() >= 1){
      return true;
    }
    return false;
  }

  private TicketHistoryRecord createTicketHistoryRecord(UUID ticketId, TicketHistoryType type, String oldVal, String newVal, UUID orgID) {
    TicketHistoryRecord ticketHistoryRecord = db.newRecord(Tables.TICKET_HISTORY);
    ticketHistoryRecord.setTicketId(ticketId);
    ticketHistoryRecord.setType(type);
    ticketHistoryRecord.setOldValue(oldVal);
    ticketHistoryRecord.setNewValue(newVal);
    ticketHistoryRecord.setCreatedAt(LocalDateTime.now());
    ticketHistoryRecord.setOrgId(orgID);
    return ticketHistoryRecord;
  }

  /**
   * Update ticket record and generate ticket history records
   *
   * @param ticketRecord
   * @param ticket
   * @return list of Ticket history records
   */
  private List<TicketHistoryRecord> updateTicketRecord(TicketRecord ticketRecord, Ticket ticket) {
    //TODO: Check for a way to use reflection to iterate through all fields.
    List<TicketHistoryRecord> historyRecords = new ArrayList<>();
    UUID ticketId = ticketRecord.getId();
    if (ticket.getAssigneeId() != null && ticketRecord.getAssigneeId() != ticket.getAssigneeId()) {
      TicketHistoryRecord historyRecord = createTicketHistoryRecord(ticketId, TicketHistoryType.assignee,
          String.valueOf(ticketRecord.getAssigneeId()),
          String.valueOf(ticket.getAssigneeId()), ticketRecord.getOrgId());
      historyRecords.add(historyRecord);

      ticketRecord.set(Tables.TICKET.ASSIGNEE_ID, ticket.getAssigneeId());
    }
    if (ticket.getStatus() != null && ticketRecord.getStatus() != ticket.getStatus()) {
      TicketHistoryRecord historyRecord = createTicketHistoryRecord(ticketId, TicketHistoryType.status,
          String.valueOf(ticketRecord.getStatus()),
          String.valueOf(ticket.getStatus()), ticketRecord.getOrgId());
      historyRecords.add(historyRecord);

      ticketRecord.set(Tables.TICKET.STATUS, ticket.getStatus());
    }
    if (ticket.getCreatorId() != null && ticketRecord.getCreatorId() != ticket.getCreatorId()) {
      ticketRecord.set(Tables.TICKET.CREATOR_ID, ticket.getCreatorId());
    }
    if (ticket.getCustomerId() != null && ticketRecord.getCustomerId() != ticket.getCustomerId()) {
      ticketRecord.set(Tables.TICKET.CUSTOMER_ID, ticket.getCustomerId());
    }
    if (ticket.getDescription() != null && ticketRecord.getDescription() != ticket.getDescription()) {
      ticketRecord.set(Tables.TICKET.DESCRIPTION, ticket.getDescription());
    }
    if (ticket.getPriority() != null && ticketRecord.getPriority() != ticket.getPriority()) {
      TicketHistoryRecord historyRecord = createTicketHistoryRecord(ticketId, TicketHistoryType.priority,
          String.valueOf(ticketRecord.getPriority()),
          String.valueOf(ticket.getPriority()), ticketRecord.getOrgId());
      historyRecords.add(historyRecord);

      ticketRecord.set(Tables.TICKET.PRIORITY, ticket.getPriority());
    }
    if (ticket.getTitle() != null && ticketRecord.getTitle() != ticket.getTitle()) {
      ticketRecord.set(Tables.TICKET.TITLE, ticket.getTitle());
    }
    if (ticket.getType() != null && ticketRecord.getType() != ticket.getType()) {
      ticketRecord.set(Tables.TICKET.TYPE, ticket.getType());
    }
    ticketRecord.set(Tables.TICKET.MODIFIED_AT, LocalDateTime.now());
    return historyRecords;
  }

  private void setOrgId(Ticket ticket) {
    if (ticket.getOrgId() == null) {
      UUID defaultOrgId = orgDbAccessor.getDefaultOrgId();
      ticket.setOrgId(defaultOrgId);
    }
  }

  @Override
  public Ticket copyOf(Optional<com.adiSuper.generated.core.tables.pojos.Ticket> optionalJooqPojo) {
    String errMessage = "Ticket not found :(";
    com.adiSuper.generated.core.tables.pojos.Ticket jooqPojo = optionalJooqPojo.orElseThrow(() -> new EntityNotFoundException(errMessage));
    return new Ticket(jooqPojo);
  }
}

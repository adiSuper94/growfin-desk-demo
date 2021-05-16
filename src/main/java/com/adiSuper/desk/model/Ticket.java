package com.adiSuper.desk.model;


import com.adiSuper.generated.core.enums.TicketPriority;
import com.adiSuper.generated.core.enums.TicketStatus;
import com.adiSuper.generated.core.enums.TicketType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;


public class Ticket extends com.adiSuper.generated.core.tables.pojos.Ticket {

  private Agent assignee, createdBy;
  private Customer customer;

  public Ticket() {
    super();
  }

  public Ticket(com.adiSuper.generated.core.tables.pojos.Ticket value) {
    super(value);
  }

  public Ticket(
      UUID id,
      String title,
      String description,
      TicketStatus status,
      TicketPriority priority,
      TicketType type,
      UUID creatorId,
      UUID assigneeId,
      UUID customerId,
      LocalDateTime createdAt,
      LocalDateTime modifiedAt,
      UUID orgId
  ) {
    super(id, title, description, status, priority, type, creatorId, assigneeId, customerId, createdAt, modifiedAt, orgId);
  }

  /**
   * Getter for <code>core.ticket.created_by</code>.
   */
  @Override @JsonIgnore
  public UUID getCreatorId() {
    return super.getCreatorId();
  }

  /**
   * Getter for <code>core.ticket.assignee</code>.
   */
  @Override @JsonIgnore
  public UUID getAssigneeId() {
    return super.getAssigneeId();
  }

  /**
   * Getter for <code>core.ticket.customer</code>.
   */

  @Override @JsonIgnore
  public UUID getCustomerId() {
    return super.getCustomerId();
  }

  /**
   * Getter for <code>core.ticket.org_id</code>.
   */
  @Override @JsonIgnore
  public UUID getOrgId() {
    return super.getOrgId();
  }

  public Customer getCustomer() {
    return customer;
  }

  /**
   * Setter for <code>customer<code/>
   * Note: this <code>customer<code/> should match the <code>customerId<code/>.
   * @param customer
   */
  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Agent getCreatedBy() {
    return createdBy;
  }

  /**
   * Setter for <code>Agent/User<code/>
   * Note: this <code>Agent<code/> should match the <code>creatorId<code/>.
   * @param createdBy
   */
  public Ticket setCreatedBy(Agent createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public Agent getAssignee() {
    return assignee;
  }

  /**
   * Setter for <code>Agent/User<code/>
   * Note: this <code>Agent<code/> should match the <code>assigneeId<code/>.
   * @param assignee
   */
  public Ticket setAssignee(Agent assignee) {
    this.assignee = assignee;
    return this;
  }
}


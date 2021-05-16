package com.adiSuper.desk.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(
    name = "response",
    schema = "core",
    uniqueConstraints = {
        @UniqueConstraint(name = "response_pkey", columnNames = { "id" })
    },
    indexes = {
        @Index(name = "response_org_id", columnList = "org_id ASC")
    }
)
public class Response implements Serializable {

  private UUID          id;
  private UUID          ticketId;
  private UUID          author;
  private String        text;
  private LocalDateTime createdAt;
  private UUID          orgId;

  public Response() {}

  public Response(Response value) {
    this.id = value.id;
    this.ticketId = value.ticketId;
    this.author = value.author;
    this.text = value.text;
    this.createdAt = value.createdAt;
    this.orgId = value.orgId;
  }

  public Response(
      UUID          id,
      UUID          ticketId,
      UUID          author,
      String        text,
      LocalDateTime createdAt,
      UUID          orgId
  ) {
    this.id = id;
    this.ticketId = ticketId;
    this.author = author;
    this.text = text;
    this.createdAt = createdAt;
    this.orgId = orgId;
  }

  /**
   * Getter for <code>core.response.id</code>.
   */
  @Id
  @Column(name = "id", nullable = false)
  public UUID getId() {
    return this.id;
  }

  /**
   * Setter for <code>core.response.id</code>.
   */
  public Response setId(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Getter for <code>core.response.ticket_id</code>.
   */
  @Column(name = "ticket_id")
  public UUID getTicketId() {
    return this.ticketId;
  }

  /**
   * Setter for <code>core.response.ticket_id</code>.
   */
  public Response setTicketId(UUID ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Getter for <code>core.response.author</code>.
   */
  @Column(name = "author")
  public UUID getAuthor() {
    return this.author;
  }

  /**
   * Setter for <code>core.response.author</code>.
   */
  public Response setAuthor(UUID author) {
    this.author = author;
    return this;
  }

  /**
   * Getter for <code>core.response.text</code>.
   */
  @Column(name = "text", nullable = false, length = 1500)
  @NotNull
  @Size(max = 1500)
  public String getText() {
    return this.text;
  }

  /**
   * Setter for <code>core.response.text</code>.
   */
  public Response setText(String text) {
    this.text = text;
    return this;
  }

  /**
   * Getter for <code>core.response.created_at</code>.
   */
  @Column(name = "created_at", nullable = false, precision = 6)
  @NotNull
  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  /**
   * Setter for <code>core.response.created_at</code>.
   */
  public Response setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Getter for <code>core.response.org_id</code>.
   */
  @Column(name = "org_id", nullable = false)
  @NotNull
  public UUID getOrgId() {
    return this.orgId;
  }

  /**
   * Setter for <code>core.response.org_id</code>.
   */
  public Response setOrgId(UUID orgId) {
    this.orgId = orgId;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Response (");

    sb.append(id);
    sb.append(", ").append(ticketId);
    sb.append(", ").append(author);
    sb.append(", ").append(text);
    sb.append(", ").append(createdAt);
    sb.append(", ").append(orgId);

    sb.append(")");
    return sb.toString();
  }
}


package com.adiSuper.desk.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TicketAssigningPolicy {

  /**
   *
   * @param ticketId
   * @return agentId
   *  or <code>null<code/> if ticket is not open if no agent found
   */
  UUID getNextAgent(UUID ticketId);

  Map<UUID, UUID> getAgentsForNextTickets(List<UUID> ticketIds);
}

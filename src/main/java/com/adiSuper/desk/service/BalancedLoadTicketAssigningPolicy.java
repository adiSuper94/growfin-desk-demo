package com.adiSuper.desk.service;

import com.adiSuper.desk.dbAccessor.AgentDbAccessor;
import com.adiSuper.desk.dbAccessor.TicketDbAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Component("balancedLoadTicketAssigningPolicy")
public class BalancedLoadTicketAssigningPolicy implements TicketAssigningPolicy {

  @Autowired
  private TicketDbAccessor ticketDbAccessor;

  @Autowired
  private AgentDbAccessor agentDbAccessor;

  @Override
  public UUID getNextAgent(UUID ticketId) {
    Map<UUID, UUID> map = this.getAgentsForNextTickets(Collections.singletonList(ticketId));
    Set<UUID> assigneeIds = map.keySet();
    if (assigneeIds.size() == 1) {
      Optional<UUID> optionalAssignee = assigneeIds.stream().findFirst();
      return optionalAssignee.orElseThrow(EntityNotFoundException::new);
    }
    return null;
  }

  @Override
  public Map<UUID, UUID> getAgentsForNextTickets(List<UUID> ticketIds) {
    List<UUID> openTicketIds = ticketDbAccessor.filterOpenTickets(ticketIds);
    Map<UUID, UUID> ticketVsAgentMap = new HashMap<>();
    if (openTicketIds.size() > 0) {
      Map<UUID, Integer> map = ticketDbAccessor.getTicketsGroupedByAssigneeId();
      for (UUID openTicketId : openTicketIds) {
        UUID leastLoadedAgentId = this.getLeastLoadedAgent(map);
        int currentLoad = map.get(leastLoadedAgentId) == null ? 0 : map.get(leastLoadedAgentId);
        ticketVsAgentMap.put(openTicketId, leastLoadedAgentId);
        map.put(leastLoadedAgentId, currentLoad + 1);
      }
    }
    return ticketVsAgentMap;
  }

  private UUID getLeastLoadedAgent(Map<UUID, Integer> agentLoadMap) {
    Optional<UUID> optionalLeastLoadedAgentId = agentLoadMap.keySet().stream().findFirst();
    UUID leastLoadedAgentId = optionalLeastLoadedAgentId.orElse(agentDbAccessor.fetchOne().getId());
    int leastLoad = Integer.MAX_VALUE;
    for (UUID agentId : agentLoadMap.keySet()) {
      int load = agentLoadMap.get(agentId);
      if (leastLoad > load) {
        leastLoad = load;
        leastLoadedAgentId = agentId;
      }
    }
    return leastLoadedAgentId;
  }
}

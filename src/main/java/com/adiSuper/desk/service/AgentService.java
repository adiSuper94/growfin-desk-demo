package com.adiSuper.desk.service;


import com.adiSuper.desk.dbAccessor.AgentDbAccessor;
import com.adiSuper.desk.model.Agent;
import com.adiSuper.generated.core.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

  @Autowired
  private AgentDbAccessor agentDbAccessor;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public Agent getAgentById(UUID id) {
    Agent agent = agentDbAccessor.fetchOne(Tables.AGENT.ID, id);
    return agent;
  }

  public List<Agent> getAllAgents() {
    List<Agent> agentList = agentDbAccessor.fetchAll();
    return agentList;
  }

  public UUID addAgent(Agent agent) {
    UUID agentId = agentDbAccessor.insertOne(agent);
    return agentId;
  }

}

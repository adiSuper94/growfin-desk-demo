package com.adiSuper.desk.dbAccessor;

import com.adiSuper.desk.model.Agent;
import com.adiSuper.generated.core.Tables;
import com.adiSuper.generated.core.tables.records.AgentRecord;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AgentDbAccessor  extends BaseDbAccessor<AgentRecord, Agent, com.adiSuper.generated.core.tables.pojos.Agent, UUID>{

  @Autowired
  private DSLContext db;

  @Autowired
  private OrgDbAccessor orgDbAccessor;

  public List<Agent> fetchAll() {
    List<Agent> agents = db.selectFrom(Tables.AGENT).fetch().into(Agent.class);
    if (agents.isEmpty()) {
      throw new EntityNotFoundException("No User found :(");
    }
    return agents;
  }

  public UUID insertOne(Agent agent) {
    this.setOrgId(agent);
    AgentRecord agentRecord = db.newRecord(Tables.AGENT, agent);
    agentRecord.changed(Tables.AGENT.ACTIVE, false);
    UUID agentId = db.insertInto(Tables.AGENT).set(agentRecord)
        .returningResult(Tables.AGENT.ID)
        .fetchOne().value1();

    return agentId;
  }

  public Agent fetchOne(){
    List<Agent>agents = db.selectFrom(Tables.AGENT).limit(1).fetch().into(Agent.class);
    if (agents.isEmpty()) {
      throw new EntityNotFoundException("No User found :(");
    }
    return agents.get(0);
  }

  private void setOrgId(Agent agent) {
    if (agent.getOrgId() == null) {
      UUID defaultOrgId = orgDbAccessor.getDefaultOrgId();
      agent.setOrgId(defaultOrgId);
    }
  }

  @Override
  public Agent copyOf(Optional<com.adiSuper.generated.core.tables.pojos.Agent> optionalJooqPojo) {
    String errMessage = "Agent not found";
    com.adiSuper.generated.core.tables.pojos.Agent jooqPojo = optionalJooqPojo.orElseThrow(() -> new EntityNotFoundException(errMessage));
    return new Agent(jooqPojo);
  }
}

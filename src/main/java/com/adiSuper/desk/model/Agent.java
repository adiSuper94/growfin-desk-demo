package com.adiSuper.desk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;


public class Agent extends com.adiSuper.generated.core.tables.pojos.Agent {

  public Agent() {
    super();
  }

  public Agent(com.adiSuper.generated.core.tables.pojos.Agent value) {
    super(value);
  }

  public Agent(UUID id, String  name, String  emailId, Boolean active, UUID orgId) {
    super(id, name, emailId, active, orgId);
  }

  @Override @JsonIgnore
  public Boolean getActive() {
    return super.getActive();
  }

  @Override @JsonIgnore
  public UUID getOrgId() {
    return super.getOrgId();
  }

}


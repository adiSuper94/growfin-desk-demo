package com.adiSuper.desk.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.UUID;


public class Customer extends com.adiSuper.generated.core.tables.pojos.Customer {


  public Customer() {
    super();
  }

  public Customer(com.adiSuper.generated.core.tables.pojos.Customer value) {
    super(value);
  }

  public Customer(UUID id, String name, String emailId, Boolean active, UUID orgId) {
    super(id, name, emailId, active, orgId);
  }

  /**
   * Getter for <code>core.customer.active</code>.
   */
  @Override
  @JsonIgnore
  public Boolean getActive() {
    return super.getActive();
  }

  /**
   * Getter for <code>core.customer.org_id</code>.
   */
  @Override @JsonIgnore
  public UUID getOrgId() {
    return super.getOrgId();
  }

  @Override
  @NotNull
  public String getName() {
    return super.getName();
  }

  @Override
  @NotNull
  public String getEmailId() {
    return super.getEmailId();
  }
}


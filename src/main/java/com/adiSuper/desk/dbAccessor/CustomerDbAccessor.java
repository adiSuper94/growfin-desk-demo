package com.adiSuper.desk.dbAccessor;

import com.adiSuper.desk.model.Customer;
import com.adiSuper.generated.core.Tables;
import com.adiSuper.generated.core.tables.records.CustomerRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerDbAccessor extends BaseDbAccessor<CustomerRecord, Customer, com.adiSuper.generated.core.tables.pojos.Customer, UUID> {

  @Autowired
  private DSLContext db;

  @Autowired
  private OrgDbAccessor orgDbAccessor;

  public List<Customer> fetchAll() {
    List<Customer> customers = db.selectFrom(Tables.CUSTOMER).fetch().into(Customer.class);
    if (customers.isEmpty()) {
      throw new EntityNotFoundException("No Customer Found :(");
    }
    return customers;
  }

  public UUID insertOne(Customer cx) {
    this.setOrgId(cx);
    CustomerRecord cxRecord = db.newRecord(Tables.CUSTOMER, cx);
    cxRecord.changed(Tables.CUSTOMER.ACTIVE, false);
    UUID customerId = db.insertInto(Tables.CUSTOMER).set(cxRecord)
        .returningResult(Tables.CUSTOMER.ID)
        .fetchOne().value1();

    return customerId;
  }

  private void setOrgId(Customer cx) {
    if (cx.getOrgId() == null) {
      UUID defaultOrgId = orgDbAccessor.getDefaultOrgId();
      cx.setOrgId(defaultOrgId);
    }
  }

  @Override
  public Customer copyOf(Optional<com.adiSuper.generated.core.tables.pojos.Customer> optionalJooqPojo) {
    String errMessage = "Customer not found";
    com.adiSuper.generated.core.tables.pojos.Customer jooqPojo = optionalJooqPojo.orElseThrow(() -> new EntityNotFoundException(errMessage));
    return new Customer(jooqPojo);
  }

}

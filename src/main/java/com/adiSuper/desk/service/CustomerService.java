package com.adiSuper.desk.service;

import com.adiSuper.desk.dbAccessor.CustomerDbAccessor;
import com.adiSuper.desk.model.Customer;
import com.adiSuper.generated.core.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

  @Autowired
  private CustomerDbAccessor customerDbAccessor;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public Customer getCustomerById(UUID customerId) {
    Customer customer = customerDbAccessor.fetchOne(Tables.CUSTOMER.ID, customerId);
    return customer;
  }

  public List<Customer> getAllCustomers() {
    return customerDbAccessor.fetchAll();
  }

  public UUID addCustomer(Customer customer) {
    return customerDbAccessor.insertOne(customer);
  }
}

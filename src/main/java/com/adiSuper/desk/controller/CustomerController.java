package com.adiSuper.desk.controller;

import com.adiSuper.desk.model.Customer;
import com.adiSuper.desk.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@RestController
public class CustomerController {

  @Autowired
  private CustomerService customerService;


  @GetMapping("/v1/customers/{id}")
  public Customer getCustomerById(@PathVariable UUID id){
    return customerService.getCustomerById(id);
  }

  @GetMapping("/v1/customers")
  public List<Customer> getCustomers(){
    return customerService.getAllCustomers();
  }

  @PostMapping("/v1/customers")
  public UUID postCustomer(@RequestBody @Valid Customer cx){
    return customerService.addCustomer(cx);
  }

  public String reflectionTest(Object object){
    String str = "";
    for(Field field: object.getClass().getDeclaredFields()){
      try {
        field.setAccessible(true);
        str = str + field.getName()
            + " - " + field.getType()
            + " - " + field.get(object) + "\n";
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return str;
  }
}

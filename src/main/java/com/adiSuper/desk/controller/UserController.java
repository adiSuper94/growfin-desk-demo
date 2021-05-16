package com.adiSuper.desk.controller;

import com.adiSuper.desk.model.Agent;
import com.adiSuper.desk.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

  @Autowired
  AgentService agentService;


  @GetMapping("/v1/users/{id}")
  public Agent getAgentById(@PathVariable UUID id){
    return agentService.getAgentById(id);
  }

  @GetMapping("/v1/users")
  public List<Agent> getAgents(){
    return agentService.getAllAgents();
  }

  @PostMapping("/v1/users")
  public UUID postAgent(@RequestBody @Valid Agent agent){
    return agentService.addAgent(agent);
  }
}

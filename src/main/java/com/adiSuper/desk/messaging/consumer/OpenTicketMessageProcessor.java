package com.adiSuper.desk.messaging.consumer;

import com.adiSuper.desk.messaging.Queues;
import com.adiSuper.desk.service.TicketService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RabbitListener(queues = Queues.OPEN_TICKET, id ="open_ticket")
public class OpenTicketMessageProcessor {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TicketService ticketService;

  @RabbitHandler
  public void assignTicket(UUID ticketId, @Header(AmqpHeaders.DELIVERY_TAG) int tag, Channel channel) throws IOException {
    logger.info("Starting ticket assigning process for ticketId:: {}", ticketId);
    ticketService.assignTicket(ticketId);
    System.out.println("TicketId::" + ticketId + " in ' open_ticket_queue' processed.");
    logger.info("Completed ticket assigning process for ticketIds:: {}", ticketId);
    channel.basicAck(tag, false);
  }

  @RabbitHandler
  public void assignTickets(List<UUID> ticketIds, @Header(AmqpHeaders.DELIVERY_TAG) int tag, Channel channel) throws IOException {
    logger.info("Starting ticket assigning process for ticketIds:: {}", ticketIds);
    ticketService.assignTickets(ticketIds);
    logger.info("Completed ticket assigning process for ticketIds:: {}", ticketIds);
    channel.basicAck(tag, true);
  }

  @RabbitHandler(isDefault = true)
  public void assignTicket(Message message, @Header(AmqpHeaders.DELIVERY_TAG) int tag, Channel channel) throws IOException {
    logger.error("{}::Incompatible Type value::{} ' open_ticket_queue'.", message.getClass(), Arrays.toString(message.getBody()));
    channel.basicAck(tag, false);
  }

}

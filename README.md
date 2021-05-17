# growfin-desk-demo
## Requirements
 Build a ticketing system that can 
- [x] Accept HTTP request 

- [x] to create a ticket 

- [x] List all the tickets 

- [x] List all the tickets filtered by 

 -  assigned agent 
 
 -  customer 
 
 -  status 
 
- [x] List details of a given ticket id 

- [x] edit details about the ticket 

- [x] update status for the ticket (open, waiting on customer, customer responded, resolved, closed) 

- [x] assign the ticket to an agent 

- [ ] to add response to the ticket 

- [x] delete the ticket 

- [ ] send email to the customer when agent adds a response 

 -  Below is a sendgrid account credentials. You can use them or you can sign up for an email service of your choice. 
 -  Note: the sendgrid credentials seems to have expired. I got an error when tried it out on it's dev portal.
 -  **Approach**: queue a message to notification exchange. And process it asynchronously.

- [x] Assign the ticket equally to available agents on that given day (do not overload a single agent, spread the ticket load equally) 

- [ ] Update tickets that were marked as Resolved status 30 days ago as closed status

 -   **Approach**: Run a cron job everyday to run a batch update.


## TODOs
 - TESTS 😬
 - Authentication and Authorization.
 - Multi-tenant support
   - Current db schema is for multi-tenant. Code needs to be changed.
 - Split unrelated components(eg. mailer) into a diff service
   
## Techstack used
 - Springboot
  - jooq
 - postgres
 - RabbitMQ

 #### [Open API Spec](api-spec.yaml)



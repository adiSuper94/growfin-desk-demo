rabbitmqadmin declare exchange name='desk.notification_exchange' type='fanout' durable=true
rabbitmqadmin declare queue name='desk.notification_queue' durable=true
rabbitmqadmin declare binding source='desk.notification_exchange' destination_type='queue' destination='desk.notification_queue'

rabbitmqadmin declare exchange name='desk.open_ticket_exchange' type='fanout' durable=true
rabbitmqadmin declare queue name='desk.open_ticket_queue' durable=true
rabbitmqadmin declare binding source='desk.open_ticket_exchange' destination_type='queue' destination='desk.open_ticket_queue'
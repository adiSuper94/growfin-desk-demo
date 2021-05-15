CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA IF NOT EXISTS core;
CREATE TYPE ticket_status AS ENUM (
  'open',
  'waiting_on_customer',
  'customer_responded',
  'resolved',
  'closed'
);

CREATE TYPE ticket_history_type AS ENUM (
  'status',
  'priority',
  'assignee'
);

CREATE TYPE ticket_priority AS ENUM (
  'p0',
  'p1',
  'p2'
);

CREATE TYPE ticket_type AS ENUM (
  'type1',
  'type2',
  'type3'
);

CREATE TABLE org (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE customer (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  name VARCHAR(100)  NOT NULL,
  email_id VARCHAR(100) NOT NULL UNIQUE,
  active BOOLEAN DEFAULT TRUE,
  org_id UUID NOT NULL,
  FOREIGN KEY (org_id) REFERENCES org(id) ON DELETE CASCADE
);

CREATE TABLE agent (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  name VARCHAR(100)  NOT NULL,
  email_id VARCHAR(100) NOT NULL UNIQUE,
  active BOOLEAN DEFAULT TRUE,
  org_id UUID NOT NULL,
  FOREIGN KEY (org_id) REFERENCES org(id) ON DELETE CASCADE
);
CREATE TABLE ticket (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  title VARCHAR(200),
  description VARCHAR(1500),
  status ticket_status NOT NULL,
  priority ticket_priority,
  created_by UUID,
  assignee UUID,
  customer UUID,
  created_at TIMESTAMP NOT NULL,
  modified_at TIMESTAMP NOT NULL,
  org_id UUID NOT NULL,
  FOREIGN KEY (org_id) REFERENCES org(id) ON DELETE CASCADE,
  FOREIGN KEY (created_by) REFERENCES agent(id) ON DELETE CASCADE,
  FOREIGN KEY (assignee) REFERENCES agent(id) ON DELETE CASCADE,
  FOREIGN KEY (customer) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE ticket_history (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  ticket_id UUID,
  author UUID NOT NULL,
  type ticket_history_type NOT NULL,
  old_value VARCHAR(1500),
  new_value VARCHAR(150) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  org_id UUID NOT NULL,
  FOREIGN KEY (org_id) REFERENCES org(id) ON DELETE CASCADE,
  FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
  FOREIGN KEY (author) REFERENCES agent(id) ON DELETE CASCADE
);

CREATE TABLE response (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  ticket_id UUID,
  author UUID,
  text VARCHAR(1500)  NOT NULL,
  created_at TIMESTAMP  NOT NULL,
  org_id UUID NOT NULL,
  FOREIGN KEY (org_id) REFERENCES org(id) ON DELETE CASCADE,
  FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
  FOREIGN KEY (author) REFERENCES agent(id) ON DELETE CASCADE
);

CREATE TABLE cx_notification (
  id UUID DEFAULT uuid_generate_v4 () PRIMARY KEY,
  template_location VARCHAR(100)  NOT NULL,
  context jsonb  NOT NULL,
  to_address UUID,
  is_sent boolean,
  org_id UUID NOT NULL,
  FOREIGN KEY (org_id) REFERENCES org(id) ON DELETE CASCADE,
  FOREIGN KEY (to_address) REFERENCES customer(id) ON DELETE CASCADE
);

-- ticket
CREATE INDEX IF NOT EXISTS ticket_assignee ON ticket (assignee, org_id);

--ticket_history
CREATE INDEX IF NOT EXISTS ticket_history_ticket_id ON ticket_history (ticket_id, org_id);

--response
CREATE INDEX IF NOT EXISTS response_org_id ON response (org_id);

--cx_notification
CREATE INDEX IF NOT EXISTS cx_notification_org_id ON cx_notification (org_id);

--agent
CREATE INDEX IF NOT EXISTS agent_org_id ON agent (org_id);

--customer
CREATE INDEX IF NOT EXISTS customer_org_id ON customer (org_id);

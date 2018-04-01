DROP SCHEMA IF EXISTS bkv CASCADE;
CREATE SCHEMA bkv;

DROP TABLE IF EXISTS bkv.user_role CASCADE;
-- DROP TABLE IF EXISTS bkv.role_permission CASCADE;
DROP TABLE IF EXISTS bkv.user_instance CASCADE;
DROP TABLE IF EXISTS bkv.user CASCADE;
DROP TABLE IF EXISTS bkv.ear_attachment CASCADE;
DROP TABLE IF EXISTS bkv.ear_message CASCADE;
DROP TABLE IF EXISTS bkv.mt940_transaction CASCADE;
DROP TABLE IF EXISTS bkv.mt940_statement CASCADE;
DROP TABLE IF EXISTS bkv.mt940_balance CASCADE;
-- DROP TABLE IF EXISTS bkv.dict_permission CASCADE;
DROP TABLE IF EXISTS bkv.dict_role CASCADE;
DROP TABLE IF EXISTS bkv.dict_ear_attachment_status CASCADE;
DROP TABLE IF EXISTS bkv.dict_balance_type CASCADE;
DROP TABLE IF EXISTS bkv.dict_funds_code CASCADE;
DROP TABLE IF EXISTS bkv.dict_result_code CASCADE;
DROP TABLE IF EXISTS bkv.dict_transaction_status CASCADE;
DROP TABLE IF EXISTS bkv.dict_instance CASCADE;

CREATE TABLE bkv.dict_ear_attachment_status
(
  code  SMALLINT NOT NULL,
  value VARCHAR  NOT NULL,
  CONSTRAINT bkv_dict_ear_attachment_status_pkey PRIMARY KEY (code)
);

CREATE TABLE bkv.dict_balance_type
(
  code  VARCHAR(1) NOT NULL,
  value VARCHAR    NOT NULL,
  CONSTRAINT bkv_dict_balance_type_pkey PRIMARY KEY (code)
);

CREATE TABLE bkv.dict_funds_code
(
  code  VARCHAR(2) NOT NULL,
  value VARCHAR    NOT NULL,
  CONSTRAINT bkv_dict_funds_code_pkey PRIMARY KEY (code)
);

CREATE TABLE bkv.dict_result_code
(
  code  SMALLINT NOT NULL,
  value VARCHAR  NOT NULL,
  CONSTRAINT bkv_dict_result_code_pkey PRIMARY KEY (code)
);

CREATE TABLE bkv.dict_transaction_status
(
  code  SMALLINT NOT NULL,
  value VARCHAR  NOT NULL,
  CONSTRAINT bkv_dict_transaction_status_pkey PRIMARY KEY (code)
);

CREATE TABLE bkv.dict_instance
(
  id      SMALLINT    NOT NULL,
  code    VARCHAR(10) NOT NULL UNIQUE,
  value   VARCHAR     NOT NULL,
  allowed BOOLEAN     NOT NULL,
  CONSTRAINT bkv_dict_instance_pkey PRIMARY KEY (id)
);

CREATE TABLE bkv.dict_role
(
  id          SMALLINT NOT NULL,
  description VARCHAR  NOT NULL,
  CONSTRAINT bkv_dict_role_pkey PRIMARY KEY (id)
);

-- CREATE TABLE bkv.dict_permission
-- (
--   id          SMALLINT NOT NULL,
--   description VARCHAR  NOT NULL,
--   CONSTRAINT bkv_dict_permission_pkey PRIMARY KEY (id)
-- );

CREATE TABLE bkv.user
(
  id          SERIAL       NOT NULL,
  login       VARCHAR(125) NOT NULL UNIQUE,
  password    VARCHAR(125) NOT NULL,
  description VARCHAR      NULL,
  super_admin BOOLEAN      NOT NULL DEFAULT FALSE,
  disabled    BOOLEAN      NOT NULL DEFAULT FALSE,
  CONSTRAINT bkv_user_pkey PRIMARY KEY (id)
);

CREATE TABLE bkv.user_role
(
  user_id INT      NOT NULL,
  role_id SMALLINT NOT NULL,
  CONSTRAINT bkv_user_role_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT bkv_user_role_user_fkey FOREIGN KEY (user_id)
  REFERENCES bkv.user (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkv_user_role_role_fkey FOREIGN KEY (role_id)
  REFERENCES bkv.dict_role (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE bkv.user_instance
(
  user_id     INT      NOT NULL,
  instance_id SMALLINT NOT NULL,
  CONSTRAINT bkv_user_instance_pkey PRIMARY KEY (user_id, instance_id),
  CONSTRAINT bkv_user_instance_user_fkey FOREIGN KEY (user_id)
  REFERENCES bkv.user (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkv_user_instance_instance_fkey FOREIGN KEY (instance_id)
  REFERENCES bkv.dict_instance (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- CREATE TABLE bkv.role_permission
-- (
--   id            SERIAL   NOT NULL,
--   role_id       SMALLINT NOT NULL,
--   permission_id SMALLINT NOT NULL,
--   CONSTRAINT bkv_role_permission_pkey PRIMARY KEY (id),
--   CONSTRAINT bkv_role_permission_role_fkey FOREIGN KEY (role_id)
--   REFERENCES bkv.role (id) MATCH SIMPLE
--   ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT bkv_role_permission_permission_fkey FOREIGN KEY (permission_id)
--   REFERENCES bkv.dict_permission (id) MATCH SIMPLE
--   ON UPDATE NO ACTION ON DELETE NO ACTION
-- );

CREATE TABLE bkv.ear_message
(
  id               BIGSERIAL   NOT NULL,
  from_list        VARCHAR,
  to_list          VARCHAR,
  subject          VARCHAR,
  sent_date        TIMESTAMPTZ,
  received_date    TIMESTAMPTZ NOT NULL,
  message_content  VARCHAR,
  attachment_count INT,
  download_path    VARCHAR,
  processing_date  TIMESTAMPTZ,
  CONSTRAINT bkv_ear_message_pkey PRIMARY KEY (id)
);

CREATE TABLE bkv.ear_attachment
(
  id            BIGSERIAL NOT NULL,
  original_name VARCHAR,
  unique_name   VARCHAR   NOT NULL,
  size          BIGINT,
  status        SMALLINT  NOT NULL,
  message_id    BIGINT,
  CONSTRAINT bkv_ear_attachment_pkey PRIMARY KEY (id),
  CONSTRAINT bkv_ear_attachment_fkey FOREIGN KEY (message_id)
  REFERENCES bkv.ear_message (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkv_ear_attachment_status_fkey FOREIGN KEY (status)
  REFERENCES bkv.dict_ear_attachment_status (code) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkv_ear_attachment_unq UNIQUE (unique_name)
);

CREATE TABLE bkv.mt940_balance
(
  id           BIGSERIAL      NOT NULL,
  amount       NUMERIC(19, 6) NOT NULL,
  balance_type VARCHAR(1)     NOT NULL,
  currency     VARCHAR(3)     NOT NULL,
  date         TIMESTAMPTZ    NOT NULL,
  CONSTRAINT bkv_mt940_balance_pkey PRIMARY KEY (id),
  CONSTRAINT bkv_mt940_balance_fkey FOREIGN KEY (balance_type)
  REFERENCES bkv.dict_balance_type (code) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE bkv.mt940_statement
(
  id                    BIGSERIAL NOT NULL,
  swift_header1         VARCHAR,
  swift_header2         VARCHAR,
  swift_header3         VARCHAR,
  account_id            VARCHAR   NOT NULL,
  statement_number      VARCHAR   NOT NULL,
  sequence_number       VARCHAR,
  transaction_reference VARCHAR   NOT NULL,
  related_reference     VARCHAR,
  opening_balance_id    BIGINT    NOT NULL,
  closing_balance_id    BIGINT    NOT NULL,
  settlement_file_id    BIGINT,
  entry_order           SMALLINT  NOT NULL DEFAULT 0,
  CONSTRAINT bkv_mt940_statement_pkey PRIMARY KEY (id),
  CONSTRAINT bkvy_mt940_opening_balance_fkey FOREIGN KEY (opening_balance_id)
  REFERENCES bkv.mt940_balance (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkvy_mt940_closing_balance_fkey FOREIGN KEY (closing_balance_id)
  REFERENCES bkv.mt940_balance (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkvy_mt940_settlement_file_fkey FOREIGN KEY (settlement_file_id)
  REFERENCES bkv.ear_attachment (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE bkv.mt940_transaction
(
  id                          BIGSERIAL      NOT NULL,
  amount                      NUMERIC(19, 6) NOT NULL,
  currency                    VARCHAR(3)     NOT NULL,
  date                        TIMESTAMPTZ    NOT NULL,
  entry_date                  TIMESTAMPTZ,
  funds_code                  VARCHAR(2),
  info_to_account_owner       VARCHAR,
  reference_for_account_owner VARCHAR        NOT NULL,
  reference_for_bank          VARCHAR,
  swift_code                  VARCHAR,
  transaction_description     VARCHAR,
  statement_id                BIGINT,
  entry_order                 SMALLINT       NOT NULL DEFAULT 0,
  status                      SMALLINT       NOT NULL,
  error_description           VARCHAR,
  instance                    SMALLINT       NOT NULL,
  CONSTRAINT bkv_mt940_transaction_pkey PRIMARY KEY (id),
  CONSTRAINT bkv_mt940_transaction_statement_fkey FOREIGN KEY (statement_id)
  REFERENCES bkv.mt940_statement (id) MATCH SIMPLE,
  CONSTRAINT bkv_mt940_transaction_funds_code_fkey FOREIGN KEY (funds_code)
  REFERENCES bkv.dict_funds_code (code) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkv_mt940_transaction_status_fkey FOREIGN KEY (status)
  REFERENCES bkv.dict_transaction_status (code) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bkv_mt940_transaction_instance_fkey FOREIGN KEY (instance)
  REFERENCES bkv.dict_instance (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

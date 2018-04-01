INSERT INTO bkv.dict_ear_attachment_status (code, value) VALUES
  (0, 'NEW'),
  (1, 'PROCESSED'),
  (2, 'ERROR');

INSERT INTO bkv.dict_balance_type (code, value) VALUES
  ('C', 'CREDIT'),
  ('D', 'DEBIT');

INSERT INTO bkv.dict_funds_code (code, value) VALUES
  ('C', 'CREDIT'),
  ('D', 'DEBIT'),
  ('RC', 'CREDIT_REVERSAL'),
  ('RD', 'DEBIT_REVERSAL');

INSERT INTO bkv.dict_instance (id, code, value, allowed) VALUES
  (-1, 'UNKNOWN', 'UNKNOWN', FALSE),
  (0, 'RUSSIA', 'RUSSIA', TRUE),
  (1, 'EUROPE', 'EUROPE', TRUE),
  (2, 'ASIA', 'ASIA', FALSE),
  (3, 'AMERICA', 'AMERICA', TRUE);

INSERT INTO bkv.dict_result_code (code, value) VALUES
  (-1, 'UNKNOWN'),
  (0, 'OK'),
  (1, 'COMMUNICATION_ERROR'),
  (2, 'INTERNAL_ERROR'),
  (3, 'MANDATORY_FIELD_IS_MISSING'),
  (4, 'UNKNOWN_REQUEST_ENTITY'),
  (5, 'SERVICE_IS_NOT_REGISTERED'),
  (6, 'INVALID_FIELD_VALUE'),
  (7, 'TRANSACTION_PROCESSING_ERROR');

INSERT INTO bkv.dict_transaction_status (code, value) VALUES
  (0, 'NEW'),
  (1, 'PROCESSED'),
  (2, 'READ'),
  (3, 'DISALLOW_PLATFORM_FEED'),
  (100, 'ERROR_BUSINESS'),
  (101, 'ERROR_BUSINESS_ACCOUNT_WAS_NOT_FOUND'),
  (102, 'ERROR_BUSINESS_ACCOUNT_IS_CLOSED'),
  (103, 'ERROR_BUSINESS_ACCOUNT_IS_FORBIDDEN'),
  (104, 'ERROR_BUSINESS_INSUFFICIENT_FUNDS'),
  (1000, 'ERROR_PROCESSING'),
  (1001, 'ERROR_PROCESSING_ALREADY_PROCESSED'),
  (1002, 'ERROR_PROCESSING_ID_WAS_NOT_FOUND'),
  (1003, 'ERROR_PROCESSING_INVALID_STATUS');

INSERT INTO bkv.dict_role (id, description) VALUES
  (0, 'SUPER_USER'), -- can all roles but not all instances
  (1, 'UI_ADMIN'), -- can all UI stuffs
  (2, 'UI_USER'),
  (3, 'UI_GUEST'),
  (100, 'API_ADMIN'), -- can all API stuffs
  (101, 'API_PING'),
  (102, 'API_GET_TRANSACTIONS'),
  (103, 'API_CONFIRM_TRANSACTIONS');

INSERT INTO bkv.user (id, login, password, description, super_admin, disabled) VALUES
  (0, 'dxall', '123', 'this monster is permitted for everething', TRUE, FALSE), -- allowed all instances, all permissions
  (1, 'admin', '$2a$10$Rj3NMGE88KJnyiutHJbNaup/spC5XV.WChz8bI4YBB4OvdYTknzGG', 'super admin', FALSE, FALSE), -- hashed pass 123
  (2, 'ui-admin', '123', 'UI admin', FALSE, FALSE),
  (3, 'api-admin', '123', 'API admin', FALSE, TRUE),
  (4, 'ui-user', '123', 'UI user', FALSE, FALSE ),
  (5, 'api-user', '123', 'API user', FALSE, FALSE);

SELECT setval('bkv.user_id_seq', 6, FALSE);

INSERT INTO bkv.user_role (user_id, role_id) VALUES
  (1, 1),
  (1, 100),
  (2, 1),
  (3, 100),
  (4, 2),
  (5, 101),
  (5, 102),
  (5, 103);

INSERT INTO bkv.user_instance (user_id, instance_id) VALUES
  (1, 0),
  (1, 1),
  (1, 3),
  (2, 0),
  (3, 0),
  (4, 0),
  (5, 0);


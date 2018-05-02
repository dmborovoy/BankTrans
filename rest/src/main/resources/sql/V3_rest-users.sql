INSERT INTO bkv.user (id, login, password, description, super_admin, disabled) VALUES
  (6, 'rest_user', '$2a$10$WTceSEBOlOE8hek3cs25Y.nfHFcMhgh6l1NtlOY/zKAk6LxnAhj5.', 'POST, DELETE, GET', FALSE, FALSE),  --pass rest_user
  (7, 'rest_admin', '$2a$10$v4Jp7v7yB3nr4vKKK57YU.raLtRVS4p925RAHjJ3dH2RZilMKnIGK', 'POST, DELETE, GET, PUT', FALSE, FALSE); --pass rest_admin

INSERT INTO bkv.user_role (user_id, role_id) VALUES
  (7, 1),  --rest_admin UI_ADMIN
  (6, 2);  --rest_user UI_USER
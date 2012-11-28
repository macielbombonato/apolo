INSERT INTO user (name, email, password) VALUES ('Administrator', 'admin@apolo.br', 'admin');

INSERT INTO user_group (name) VALUES ('Administrators');
INSERT INTO user_group (name) VALUES ('Users');

INSERT INTO users_in_groups (user_id, group_id) VALUES (1, 1);

INSERT INTO group_permission (group_id, permission_name) VALUES (1, 'ADMIN'); 
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER');
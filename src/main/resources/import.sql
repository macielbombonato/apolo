INSERT INTO user_group (name) VALUES ('Administrators');
INSERT INTO user_group (name) VALUES ('Users');

INSERT INTO group_permission (group_id, permission_name) VALUES (1, 'ADMIN'); 
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER');

INSERT INTO user (name, email, password) VALUES ('Administrator', 'admin@apolo.br', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO user (name, email, password) VALUES ('User', 'user@apolo.br', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO users_in_groups (user_id, group_id) VALUES (1, 1);
INSERT INTO users_in_groups (user_id, group_id) VALUES (2, 2);
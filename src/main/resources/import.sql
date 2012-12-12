INSERT INTO user_group (name) VALUES ('Administrators');
INSERT INTO user_group (name) VALUES ('Super Users');
INSERT INTO user_group (name) VALUES ('Restricted Users');

INSERT INTO group_permission (group_id, permission_name) VALUES (1, 'ADMIN'); 

INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER'); 
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_CREATE'); 
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_EDIT');
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_REMOVE');
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_LIST');
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_PERMISSION_LIST');
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_PERMISSION_CREATE');
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_PERMISSION_EDIT');
INSERT INTO group_permission (group_id, permission_name) VALUES (2, 'USER_PERMISSION_REMOVE');

INSERT INTO group_permission (group_id, permission_name) VALUES (3, 'USER');

INSERT INTO user (name, email, password) VALUES ('Administrator', 'admin@apolo.br', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO user (name, email, password) VALUES ('Super user', 'user@apolo.br', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO user (name, email, password) VALUES ('Restricted User', 'restr@apolo.br', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO users_in_groups (user_id, group_id) VALUES (1, 1);
INSERT INTO users_in_groups (user_id, group_id) VALUES (2, 2);
INSERT INTO users_in_groups (user_id, group_id) VALUES (3, 3);
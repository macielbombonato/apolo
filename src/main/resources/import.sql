INSERT INTO user_group (name) VALUES ('Administrators');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Administrators'), 'ADMIN');

INSERT INTO user_group (name) VALUES ('Users - Gerenciamento');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Gerenciamento'), 'USER'); 
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Gerenciamento'), 'USER_CREATE'); 
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Gerenciamento'), 'USER_EDIT');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Gerenciamento'), 'USER_REMOVE');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Gerenciamento'), 'USER_LIST');

INSERT INTO user_group (name) VALUES ('Users');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users'), 'USER');

INSERT INTO user_group (name) VALUES ('Permissions - Gerenciamento');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Gerenciamento'), 'USER_PERMISSION_LIST');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Gerenciamento'), 'USER_PERMISSION_CREATE');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Gerenciamento'), 'USER_PERMISSION_EDIT');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Gerenciamento'), 'USER_PERMISSION_REMOVE');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Gerenciamento'), 'USER_PERMISSION_VIEW');

INSERT INTO user_group (name) VALUES ('Permissions');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions'), 'USER_PERMISSION_LIST');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions'), 'USER_PERMISSION_VIEW');


INSERT INTO user (name, email, password) VALUES ('Administrator', 'admin@apolo.br', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'admin@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Administrators'));

INSERT INTO user (name, email, password) VALUES ('Power user', 'user@apolo.br', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'user@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Users - Gerenciamento'));
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'user@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Permissions - Gerenciamento'));

INSERT INTO user (name, email, password) VALUES ('User', 'restr@apolo.br', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'restr@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Users'));

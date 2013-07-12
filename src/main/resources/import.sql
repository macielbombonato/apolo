INSERT INTO user (name, email, password, created_by, creation_dt) VALUES ('Administrator', 'admin@apolo.br', '21232f297a57a5a743894a0e4a801fc3', 1, NOW());

INSERT INTO user_group (name, created_by, creation_dt) VALUES ('Administrators', 1, NOW());
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Administrators'), 'ADMIN');

INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'admin@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Administrators'));


INSERT INTO user_group (name, created_by, creation_dt) VALUES ('Users - Management', 1, NOW());
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Management'), 'USER_CREATE'); 
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Management'), 'USER_EDIT');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Management'), 'USER_REMOVE');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users - Management'), 'USER_LIST');

INSERT INTO user_group (name, created_by, creation_dt) VALUES ('Users', 1, NOW());
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Users'), 'USER_LIST');

INSERT INTO user_group (name, created_by, creation_dt) VALUES ('Permissions - Management', 1, NOW());
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Management'), 'USER_PERMISSION_LIST');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Management'), 'USER_PERMISSION_CREATE');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Management'), 'USER_PERMISSION_EDIT');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Management'), 'USER_PERMISSION_REMOVE');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions - Management'), 'USER_PERMISSION_VIEW');

INSERT INTO user_group (name, created_by, creation_dt) VALUES ('Permissions', 1, NOW());
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions'), 'USER_PERMISSION_LIST');
INSERT INTO group_permission (group_id, permission_name) VALUES ((SELECT user_group_id FROM user_group WHERE name = 'Permissions'), 'USER_PERMISSION_VIEW');


INSERT INTO user (name, email, password, created_by, creation_dt) VALUES ('Power user', 'user@apolo.br', '21232f297a57a5a743894a0e4a801fc3', 1, NOW());
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'user@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Users - Management'));
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'user@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Permissions - Management'));

INSERT INTO user (name, email, password, created_by, creation_dt) VALUES ('User', 'restr@apolo.br', '21232f297a57a5a743894a0e4a801fc3', 1, NOW());
INSERT INTO users_in_groups (user_id, group_id) VALUES ((SELECT user_id FROM user WHERE email = 'restr@apolo.br'), (SELECT user_group_id FROM user_group WHERE name = 'Users'));

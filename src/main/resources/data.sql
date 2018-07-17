-- password: password
INSERT INTO USER (TOKEN_EXPIRED, ENABLED,ID,USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL) VALUES
  (TRUE ,TRUE,10,'username_admin','$2a$10$6VDQry1ZymBtx7DGgjBh5u4njN5xqJHrvt1TCARL/Nmib9bPjlp1e', 'first_name_admin','last_name_admin','adminek@gmail.com'),
  (TRUE ,TRUE,11,'username_standard','$2a$10$6VDQry1ZymBtx7DGgjBh5u4njN5xqJHrvt1TCARL/Nmib9bPjlp1e', 'first_name_standard','last_name_standard','standard@gmail.com');

INSERT INTO ROLE (ID,NAME) VALUES
  (10,'ROLE_ADMIN'),
  (11,'ROLE_STANDARD');

INSERT INTO PERMISSION(ID, NAME) VALUES
  (10, 'perm1'),
  (11, 'perm2'),
  (12, 'perm3'),
  (13, 'perm4');
INSERT INTO ROLE_PERMISSIONS(ROLE_ID,PERMISSION_ID) VALUES
  (10,10),
  (10,11),
  (11,12),
  (11,13);
INSERT INTO USER_ROLES(USER_ID,ROLE_ID) VALUES
  (10,10),
  (10,11),
  (11,11);
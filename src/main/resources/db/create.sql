CREATE TABLE user
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(45) NOT NULL,
  login_attempts INT(11) DEFAULT '0',
  locked TINYINT(1) DEFAULT '0' NOT NULL
);
CREATE TABLE messages
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  author VARCHAR(30) DEFAULT 'ANONYMOUS' NOT NULL,
  message VARCHAR(500) NOT NULL,
  clave VARCHAR(100) NOT NULL
);
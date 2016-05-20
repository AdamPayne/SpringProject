DROP TABLE IF EXISTS `TEL`.`contacts`;
DROP TABLE IF EXISTS `TEL`.`search`;
DROP TABLE IF EXISTS `TEL`.`users`;

CREATE TABLE `TEL`.`users` (  
  `id` int(6) NOT NULL AUTO_INCREMENT,  
  `login` varchar(20) NOT NULL,  
  `password` varchar(20) NOT NULL,  
  PRIMARY KEY (`id`) ,
  UNIQUE KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO tel.users (login, password) VALUES ('root', 'admin'), ('adm', 'adm');   

CREATE TABLE  `TEL`.`contacts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `address` varchar(45) DEFAULT NULL,
  `gender` char(1) DEFAULT 'M',
  `dob` datetime DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY(`username`)
  REFERENCES users(`login`)
  ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO tel.contacts (name, address, gender, dob, email, mobile, username) VALUES ('Dimitar', 'a', 'm', '1995-08-08 00:00:00', 'a@a.a', '0893860231', 'root'), ('Krisi', 'b', 'f', '1995-08-01 00:00:00', 'b@b.b', '0881234567', 'root');

CREATE TABLE `TEL`.`search` (  
  `query` varchar(45) NOT NULL,  
  `doa` datetime DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`query`, `username`),
  FOREIGN KEY(`username`)
  REFERENCES users(`login`)
  ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO tel.search (query, doa, username) VALUES ('Dim', '2000-09-10 00:00:00', 'root'), ('K', '2001-08-18 00:00:00', 'root'), ('A', '2000-09-10 00:00:00', 'adm');
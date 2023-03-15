
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
);

CREATE TABLE `appaccount` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_balance` double DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `appaccount_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
);

CREATE TABLE `connection` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author` int NOT NULL,
  `target` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `relationship_not_repeated` (`author`,`target`),
  KEY `target` (`target`),
  CONSTRAINT `connection_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `connection_ibfk_2` FOREIGN KEY (`target`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ids_not_equal` CHECK ((`author` <> `target`))
);

CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `credited_account` int NOT NULL,
  `debited_account` int NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `amount_of_transaction` decimal(10,2) DEFAULT NULL,
  `cost_of_transaction` decimal(10,2) DEFAULT NULL,
  `date_time` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `debited_account` (`debited_account`),
  KEY `credited_account` (`credited_account`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`debited_account`) REFERENCES `appaccount` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`credited_account`) REFERENCES `appaccount` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ids_not_equal2` CHECK ((`credited_account` <> `debited_account`))
);


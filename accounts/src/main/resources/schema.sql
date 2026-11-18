
-- Spring boot can automatically executes the below two files when the app starts
-- 1. schema.sql (creates tables) : creates db structure like tables, indexes, constraints. When the
-- app starts then Spring boot executes schema.sql first (if SQL initialization is enabled)
-- 2. data.sql (insert data) : insert initial data into the tables created by schema.sql. This will executes
-- after the schema.sql


CREATE TABLE IF NOT EXISTS `customer` (
`customer_id` int AUTO_INCREMENT PRIMARY KEY,
`name` varchar(100) NOT NULL,
`email` varchar(100) NOT NULL,
`mobile_number` varchar(20) NOT NULL,
`created_at` date NOT NULL,
`created_by` varchar(20) NOT NULL,
`updated_at` date DEFAULT NULL,
`updated_by` varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `accounts` (
`customer_id` int NOT NULL,
`account_number` int AUTO_INCREMENT PRIMARY KEY,
`account_type` varchar(100) NOT NULL,
`branch_address` varchar(200) NOT NULL,
`created_at` date NOT NULL,
`created_by` varchar(20) NOT NULL,
`updated_at` date DEFAULT NULL,
`updated_by` varchar(20) DEFAULT NULL
);
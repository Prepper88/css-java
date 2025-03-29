-- MySQL dump 10.13  Distrib 9.0.1, for macos14.4 (arm64)
--
-- Host: localhost    Database: css_java
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `agent`
--

LOCK TABLES `agent` WRITE;
/*!40000 ALTER TABLE `agent` DISABLE KEYS */;
INSERT INTO `agent` VALUES (1,1,'202cb962ac59075b964b07152d234b70',NULL,'yyy'),(2,2,'202cb962ac59075b964b07152d234b70',NULL,'chase'),(3,3,'202cb962ac59075b964b07152d234b70',NULL,'lzj'),(4,4,'202cb962ac59075b964b07152d234b70',NULL,'ztf');
/*!40000 ALTER TABLE `agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'202cb962ac59075b964b07152d234b70','yyy', 'Yuanyuan Yang', '+1 2387652345', 'yuanyuan.yang@uwindsor.ca', '2000-01-01'),(2,'202cb962ac59075b964b07152d234b70','chase', 'Chase Shen', '+1 2387652345', 'chase.shen@uwindsor.ca', '2000-01-02'),(3,'202cb962ac59075b964b07152d234b70','lzj', 'Ziji Liu', '+1 2387652345', 'ziji.liu@uwindsor.ca', '2000-01-03'),(4,'202cb962ac59075b964b07152d234b70','ztf', 'Tengfei Zhu', '+1 2387652345', 'tengfei.zhu@uwindsor.ca', '2000-01-04');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO css_java.orders (created_at, customer_id, id, image_name, order_id, order_title, price, pay_status, delivery_status) VALUES ('2025-03-27 16:30:53.000000', 1, 1, 'cake.png', '123456789', 'A tasty cake', '134.5', 'paid', 'delivered');
INSERT INTO css_java.orders (created_at, customer_id, id, image_name, order_id, order_title, price, pay_status, delivery_status) VALUES ('2025-03-28 10:40:12.000000', 1, 2, 'apple.png', '3281923', 'Apples', '23.00', 'unpaid', 'undelivered');
INSERT INTO css_java.orders (created_at, customer_id, id, image_name, order_id, order_title, price, pay_status, delivery_status) VALUES ('2025-03-29 08:31:47.000000', 1, 3, 'bell.png', '76548762', 'Bell Home Internet', '59.99/Mon.', 'paid', 'undelivered');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `order_field` WRITE;
/*!40000 ALTER TABLE `order_field` DISABLE KEYS */;
INSERT INTO css_java.order_field (id, name, order_id, type, value) VALUES (1, 'Technician Name', '76548762', 'text', 'Bill Jome');
INSERT INTO css_java.order_field (id, name, order_id, type, value) VALUES (2, 'Technician Phone', '76548762', 'text', '+1 888-555-9999');
INSERT INTO css_java.order_field (id, name, order_id, type, value) VALUES (3, 'Appointment Time', '76548762', 'text', '2025-03-26 8:00AM - 12:00AM');
INSERT INTO css_java.order_field (id, name, order_id, type, value) VALUES (4, 'Device Fee', '76548762', 'text', '$100');
INSERT INTO css_java.order_field (id, name, order_id, type, value) VALUES (5, 'Status', '76548762', 'text', 'uninstalled');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `session`
--

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-26 14:34:58

-- MySQL dump 10.13  Distrib 9.2.0, for macos15 (arm64)
--
-- Host: localhost    Database: ebookshop
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Current Database: `ebookshop`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ebookshop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ebookshop`;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `genre` varchar(50) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `qty` int DEFAULT NULL,
  `description` text,
  `cover_image_url` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'Harry Potter and the Sorcerer\'s Stone','J.K. Rowling','Fantasy',19.99,98,NULL,'https://m.media-amazon.com/images/I/81DI+BAN2SL._SY522_.jpg'),(2,'Harry Potter and the Chamber of Secrets','J.K. Rowling','Fantasy',19.99,90,NULL,'https://m.media-amazon.com/images/I/51NUYJDvkgL._SY445_SX342_.jpg'),(3,'1984','George Orwell','Dystopian',15.99,146,NULL,'https://m.media-amazon.com/images/I/41KzoY18SyL._SY445_SX342_.jpg'),(4,'The Hobbit','J.R.R. Tolkien','Fantasy',12.99,196,NULL,'https://m.media-amazon.com/images/I/51QnRBHJ+sL._SY445_SX342_.jpg'),(5,'The Quite Nice and Fairly Accurate Good Omens','Neil Gaiman','Fantasy',14.99,75,NULL,'https://m.media-amazon.com/images/I/71-G8t2nQeL._SY522_.jpg'),(6,'Fantastic Beasts and Where to Find Them','J.K. Rowling','Fantasy',10.99,74,NULL,'https://m.media-amazon.com/images/I/51LIgWAVG+L._SY445_SX342_.jpg'),(7,'To Kill a Mockingbird','Harper Lee','Classic',10.99,120,NULL,'https://m.media-amazon.com/images/I/81aY1lxk+9L._SY522_.jpg'),(8,'Pride and Prejudice','Jane Austen','Classic',8.99,89,NULL,'https://m.media-amazon.com/images/I/61BahDaTZXL._SY445_SX342_.jpg'),(9,'The Great Gatsby','F. Scott Fitzgerald','Classic',11.99,78,NULL,'https://m.media-amazon.com/images/I/41NssxNlPlS._SY445_SX342_.jpg'),(10,'The Catcher in the Rye','J.D. Salinger','Classic',9.99,127,NULL,'https://m.media-amazon.com/images/I/518dVCGFuhL._SY445_SX342_.jpg');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorites` (
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`book_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `order_id` int NOT NULL,
  `book_id` int NOT NULL,
  `qty` int DEFAULT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`order_id`,`book_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_records` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,1,19.99),(1,2,2,19.99),(1,3,1,15.99),(2,1,1,19.99),(2,2,2,19.99),(2,3,1,15.99),(8,2,1,19.99),(8,4,1,12.99),(8,9,1,11.99),(9,2,1,19.99),(9,4,1,12.99),(9,9,1,11.99),(10,3,1,15.99),(10,6,1,10.99),(11,2,1,19.99),(11,3,1,15.99),(11,4,2,12.99),(12,2,1,19.99),(12,10,3,9.99),(13,2,2,19.99),(13,8,1,8.99);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_records`
--

DROP TABLE IF EXISTS `order_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_records` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) DEFAULT NULL,
  `user_email` varchar(30) DEFAULT NULL,
  `user_phone` char(8) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `order_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_price` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `order_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_records`
--

LOCK TABLES `order_records` WRITE;
/*!40000 ALTER TABLE `order_records` DISABLE KEYS */;
INSERT INTO `order_records` VALUES (1,'test','null','null',NULL,'2025-03-12 12:22:40',75.96),(2,'test','null','null',NULL,'2025-03-12 12:31:24',75.96),(8,'test','email@gmail.com','81569920',NULL,'2025-03-12 18:03:31',44.97),(9,'test','email@gmail.com','81569920',NULL,'2025-03-13 12:35:17',44.97),(10,'John','john@gmail.com','88882222',NULL,'2025-03-13 12:43:15',26.98),(11,'John','123@gmail.com','11111111',NULL,'2025-03-13 14:40:25',61.96),(12,'Pham Minh Anh','minhanh@gmail.com','66666666',NULL,'2025-03-13 15:11:45',49.96),(13,'hahaha','email.com','33333333',NULL,'2025-03-14 02:56:29',48.97);
/*!40000 ALTER TABLE `order_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Pham Minh Anh','test_pw','2025-02-21 02:19:56'),(2,'testUser','password123','2025-03-04 08:09:24'),(4,'test','12345678','2025-03-04 17:40:33'),(5,'John','12345678','2025-03-05 05:42:14'),(6,'kitty','88888888','2025-03-14 01:41:08'),(7,'hihihi','hahaha','2025-03-14 01:49:09'),(8,'hahaha','huhuhu','2025-03-14 02:54:06');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-14 11:12:16

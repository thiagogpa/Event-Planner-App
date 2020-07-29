CREATE DATABASE  IF NOT EXISTS `id12635042_androidapp` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `id12635042_androidapp`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: id12635042_androidapp
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(256) NOT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` (`id`, `name`) VALUES (1,'Food'),(2,'Drinks'),(3,'Others');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `description` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` (`id`, `description`) VALUES (1,'Barbecue'),(2,'Birthday'),(3,'BUSINESS_MEETING');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_product`
--

DROP TABLE IF EXISTS `event_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_product` (
                                 `event_id` int DEFAULT NULL,
                                 `product_id` int DEFAULT NULL,
                                 KEY `event_product_event_id_fk` (`event_id`),
                                 KEY `event_product_products_id_fk` (`product_id`),
                                 CONSTRAINT `event_product_event_id_fk` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
                                 CONSTRAINT `event_product_products_id_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_product`
--

LOCK TABLES `event_product` WRITE;
/*!40000 ALTER TABLE `event_product` DISABLE KEYS */;
INSERT INTO `event_product` (`event_id`, `product_id`) VALUES (1,1),(1,2),(1,3),(1,5),(1,6),(1,7),(1,12),(1,13),(1,14),(1,15),(1,17),(1,19),(1,20),(1,21),(1,23),(1,24),(1,25),(1,26),(1,27),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,36),(1,37),(1,38),(1,40),(1,41),(1,42),(1,43),(1,47),(1,48),(1,50),(2,4),(2,8),(2,9),(2,10),(2,11),(2,12),(2,16),(2,17),(2,18),(2,21),(2,22),(2,24),(2,28),(2,32),(2,33),(2,34),(2,35),(2,36),(2,37),(2,38),(2,39),(2,40),(2,41),(2,42),(2,43),(2,44),(2,45),(2,46),(2,47),(2,48),(2,49),(2,50),(3,8),(3,9),(3,10),(3,11),(3,12),(3,18),(3,22),(3,28),(3,35),(3,36),(3,39),(3,41),(3,45),(3,50);
/*!40000 ALTER TABLE `event_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guests`
--

DROP TABLE IF EXISTS `guests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guests` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `description` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guests`
--

LOCK TABLES `guests` WRITE;
/*!40000 ALTER TABLE `guests` DISABLE KEYS */;
INSERT INTO `guests` (`id`, `description`) VALUES (1,'Men'),(2,'Women'),(3,'Children');
/*!40000 ALTER TABLE `guests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(32) NOT NULL,
                            `price` decimal(10,2) NOT NULL,
                            `category_id` int NOT NULL,
                            `image_url` varchar(255) NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` (`id`, `name`, `price`, `category_id`, `image_url`) VALUES (1,'Bacon',8.15,1,'bacon.png'),(2,'Beef',4.14,1,'beef.png'),(3,'Buffalo',1.04,1,'buffalo.png'),(4,'Cake',13.08,1,'cake.png'),(5,'Chicken',2.04,1,'chicken.png'),(6,'Chicken Leg',12.02,1,'chicken_leg.png'),(7,'Chicken Wings',2.19,1,'chicken_wings.png'),(8,'Cookie',5.17,1,'cookie.png'),(9,'Cracker',13.19,1,'cracker.png'),(10,'Croissant',6.18,1,'croissant.png'),(11,'Fruit',13.20,1,'fruit.png'),(12,'Hotdog',12.07,1,'hotdog.png'),(13,'Lamb',13.16,1,'lamb.png'),(14,'Meat',16.00,1,'meat.png'),(15,'Meatloaf',17.03,1,'meatloaf.png'),(16,'Muffin',15.19,1,'muffin.png'),(17,'Noodle Soup',11.16,1,'noodle_soup.png'),(18,'Pie',6.18,1,'pie.png'),(19,'Pork Leg',13.08,1,'pork_leg.png'),(20,'Premium Sausage',19.18,1,'premium_sausage.png'),(21,'Red Beans',1.05,1,'red_beans.png'),(22,'Refreshment',4.03,1,'refreshment.png'),(23,'Ribs',18.06,1,'ribs.png'),(24,'Rice',14.06,1,'rice.png'),(25,'Roasted Chicken',7.17,1,'roasted_chicken.png'),(26,'Salami',10.09,1,'salami.png'),(27,'Salt',11.13,1,'salt.png'),(28,'Sandwich',16.07,1,'sandwich.png'),(29,'Sausage',15.18,1,'sausage.png'),(30,'Sirloin Steak',17.00,1,'sirloin_steak.png'),(31,'Steak',10.10,1,'steak.png'),(32,'Absinthe',9.08,2,'absinthe.png'),(33,'Beer',12.05,2,'beer.png'),(34,'Cocktail',19.10,2,'cocktail.png'),(35,'Coffee',17.08,2,'coffee.png'),(36,'Juice',10.09,2,'juice.png'),(37,'Martini',17.13,2,'martini.png'),(38,'Sangria',17.07,2,'sangria.png'),(39,'Tea',14.10,2,'tea.png'),(40,'Vodka',20.00,2,'vodka.png'),(41,'Water',10.12,2,'water.png'),(42,'Whiskey',17.03,2,'whiskey.png'),(43,'Wine Bottle',19.00,2,'wine_bottle.png'),(44,'Ballons',15.18,3,'ballons.png'),(45,'Bartender',2.08,3,'bartender.png'),(46,'Clown',5.19,3,'clown.png'),(47,'Cook',19.17,3,'cook.png'),(48,'Cutlery',3.07,3,'cutlery.png'),(49,'Magician',20.17,3,'magician.png'),(50,'Plastic Cup',10.19,3,'plastic_cup.png');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proportions`
--

DROP TABLE IF EXISTS `proportions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proportions` (
                               `product_id` int NOT NULL,
                               `guest_id` int NOT NULL,
                               `quantity` double NOT NULL,
                               KEY `proportions_guests_id_fk` (`guest_id`),
                               KEY `proportions_products_id_fk` (`product_id`),
                               CONSTRAINT `proportions_guests_id_fk` FOREIGN KEY (`guest_id`) REFERENCES `guests` (`id`),
                               CONSTRAINT `proportions_products_id_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proportions`
--

LOCK TABLES `proportions` WRITE;
/*!40000 ALTER TABLE `proportions` DISABLE KEYS */;
INSERT INTO `proportions` (`product_id`, `guest_id`, `quantity`) VALUES (1,1,0.38),(2,1,0.38),(3,1,0.35),(4,1,0.8),(5,1,0.17),(6,1,0.83),(7,1,0.86),(8,1,0.41),(9,1,0.22),(10,1,0.26),(11,1,0.22),(12,1,0.58),(13,1,0.58),(14,1,0.93),(15,1,0.63),(16,1,0.17),(17,1,0.36),(18,1,0.03),(19,1,0.39),(20,1,0.34),(21,1,0.62),(22,1,0.03),(23,1,0.31),(24,1,0.33),(25,1,0.25),(26,1,0.75),(27,1,0.47),(28,1,0.71),(29,1,0.14),(30,1,0.61),(31,1,0.17),(32,1,0.14),(33,1,0.42),(34,1,0.46),(35,1,0.24),(36,1,0.61),(37,1,0.41),(38,1,0.99),(39,1,0.79),(40,1,0.14),(41,1,0.81),(42,1,0.04),(43,1,0.36),(44,1,0.34),(45,1,0.93),(46,1,0.2),(47,1,0.14),(48,1,0.11),(49,1,0.27),(50,1,0.83),(1,2,0.23),(2,2,0.11),(3,2,0.93),(4,2,0.96),(5,2,0.21),(6,2,0),(7,2,0.68),(8,2,0.1),(9,2,0.19),(10,2,0.84),(11,2,0.61),(12,2,0.82),(13,2,0.52),(14,2,0.79),(15,2,0.13),(16,2,0.54),(17,2,0.65),(18,2,0.1),(19,2,0.37),(20,2,0.63),(21,2,0.06),(22,2,0.7),(23,2,0.81),(24,2,0.38),(25,2,0.43),(26,2,0.89),(27,2,0.38),(28,2,0.85),(29,2,0.58),(30,2,0.07),(31,2,0.82),(32,2,0.64),(33,2,0.8),(34,2,0.32),(35,2,0.98),(36,2,0.62),(37,2,0.68),(38,2,0.01),(39,2,0.35),(40,2,0.21),(41,2,0.03),(42,2,0.45),(43,2,0.56),(44,2,0.99),(45,2,0.2),(46,2,0.41),(47,2,0.47),(48,2,0.72),(49,2,0.9),(50,2,0.2),(1,3,0.35),(2,3,0.68),(3,3,0.43),(4,3,0.56),(5,3,0.7),(6,3,0.34),(7,3,0.29),(8,3,0.75),(9,3,0.74),(10,3,0.31),(11,3,0.88),(12,3,0.3),(13,3,0.19),(14,3,0.85),(15,3,0.27),(16,3,0.82),(17,3,0.91),(18,3,0.52),(19,3,0.08),(20,3,0.79),(21,3,0.64),(22,3,0.07),(23,3,0.44),(24,3,0.34),(25,3,0.87),(26,3,0.04),(27,3,0.42),(28,3,0.03),(29,3,0.18),(30,3,0.27),(31,3,0.94),(32,3,0.75),(33,3,0.57),(34,3,0.11),(35,3,0.28),(36,3,0.26),(37,3,0.97),(38,3,0.19),(39,3,0.46),(40,3,0.8),(41,3,0.92),(42,3,0.51),(43,3,0.71),(44,3,0.67),(45,3,0.71),(46,3,0.79),(47,3,0.63),(48,3,0.83),(49,3,0.34),(50,3,0.34);
/*!40000 ALTER TABLE `proportions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-13  0:38:13

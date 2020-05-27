CREATE DATABASE  IF NOT EXISTS `bibliosDB` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bibliosDB`;
-- MySQL dump 10.13  Distrib 8.0.19, for Linux (x86_64)
--
-- Host: localhost    Database: bibliosDB
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
-- Table structure for table `Book`
--

DROP TABLE IF EXISTS `Book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Book` (
  `ISBN` bigint NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `numCopies` int NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Book`
--

LOCK TABLES `Book` WRITE;
/*!40000 ALTER TABLE `Book` DISABLE KEYS */;
INSERT INTO `Book` VALUES (0,'Manuale del trapano','Mario Dilaria',4,'Manuals'),(7,'Mondo senza Fine','Ken Follett',2,'History'),(21,'Ivanhoe','Walter Scott',2,'History'),(465,'Tutto per amore','Catherine Dunne',2,'Tragic'),(516,'Il Signore degli Anelli','J.R.R. Tolkien',5,'Fantasy'),(920,'La notte più lunga','Michael Connelly',3,'Literature'),(923,'Lo Hobbit','J.R.R Tolkien',2,'Fantasy'),(956,'Lezioni di Elettrotecnica','Marco Raugi',1,'School'),(4865,'Harry Potter','J.K. Rowling',4,'Fantasy'),(7412,'Il Cacciatore di Aquiloni','Khaled Hosseini',1,'Romance'),(7814,'Vado a Torino','Joseph Pimpinella',3,'Tragic'),(7863,'L\'Orlando Furioso','Ludovico Ariosto',2,'Literature'),(8956,'Consigli di Moda','Joseph Pimpinella',1,'Humor'),(9862,'Dallo Zar alla Rivoluzione Bolscevica','Orghei Danielovich',2,'History'),(12423,'Hyperversum','Cecilia Randall',2,'Fantasy'),(16512,'Le Cronache di Narnia','C.S. Lewis',1,'Fantasy'),(54678,'Tennis per Principianti','Roger Orgallo',1,'Sport'),(74961,'Io, Ibra','David Lagercrantz',2,'Sport'),(97898,'La Divina Commedia','Dante Alighieri',3,'Epic'),(143360,'Dalle porte AND, OR, NOT al sistema Calcolatore','Paolo Corsini',3,'School'),(523621,'Fondamenti di Programmazione Web','Francesco Barbarulo',1,'School');
/*!40000 ALTER TABLE `Book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Loan`
--

DROP TABLE IF EXISTS `Loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Loan` (
  `status` int DEFAULT '0',
  `book_ISBN` bigint NOT NULL,
  `user_idUser` varchar(255) NOT NULL,
  PRIMARY KEY (`book_ISBN`,`user_idUser`),
  KEY `FK8jk80wd9xe4pei6m4t08juhyb` (`user_idUser`),
  KEY `fk_Loan_1_idx` (`status`),
  CONSTRAINT `FK8jk80wd9xe4pei6m4t08juhyb` FOREIGN KEY (`user_idUser`) REFERENCES `User` (`idUser`),
  CONSTRAINT `fk_Loan_1` FOREIGN KEY (`status`) REFERENCES `LoanStatus` (`idLoanStatus`),
  CONSTRAINT `FKdqrn59ir0vyvhvk2aiebukj3e` FOREIGN KEY (`book_ISBN`) REFERENCES `Book` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Loan`
--

LOCK TABLES `Loan` WRITE;
/*!40000 ALTER TABLE `Loan` DISABLE KEYS */;
INSERT INTO `Loan` VALUES (0,0,'AR5050101'),(0,0,'AR5050102'),(0,0,'bbb2'),(0,516,'bbb2'),(0,956,'bbb2'),(0,4865,'AR5050102'),(0,7814,'bbb2'),(1,21,'bbb2'),(1,516,'AR5050102'),(1,16512,'bbb2'),(1,54678,'bbb2'),(2,516,'AR5050103'),(2,8956,'bbb2');
/*!40000 ALTER TABLE `Loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LoanStatus`
--

DROP TABLE IF EXISTS `LoanStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LoanStatus` (
  `idLoanStatus` int NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`idLoanStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LoanStatus`
--

LOCK TABLES `LoanStatus` WRITE;
/*!40000 ALTER TABLE `LoanStatus` DISABLE KEYS */;
INSERT INTO `LoanStatus` VALUES (0,'Requested'),(1,'Granted'),(2,'Returning');
/*!40000 ALTER TABLE `LoanStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Privilege`
--

DROP TABLE IF EXISTS `Privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Privilege` (
  `id` tinyint NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Privilege`
--

LOCK TABLES `Privilege` WRITE;
/*!40000 ALTER TABLE `Privilege` DISABLE KEYS */;
INSERT INTO `Privilege` VALUES (0,'Customer'),(1,'Bibliotecary');
/*!40000 ALTER TABLE `Privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `idUser` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `privilege` tinyint DEFAULT '0',
  `idNode` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `idNode_UNIQUE` (`idNode`),
  KEY `fk_User_1_idx` (`privilege`),
  CONSTRAINT `fk_User_1` FOREIGN KEY (`privilege`) REFERENCES `Privilege` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES ('aaa1','Mario','Krapikas',1,1),('AR5012715','Giulio','Orgallo',1,2),('AR5050101','Gerardo','Macchi',0,3),('AR5050102','Riccardo','Tek',0,4),('AR5050103','Marco','Dilaria',0,5),('bbb2','Alessio','Bongio',0,6),('ccc1','Mario','Pimpinella',0,7),('ddd1','Daniele','Alvaro',0,8),('qngfbv','Mario','Rossi',0,9);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `VistaNomi`
--

DROP TABLE IF EXISTS `VistaNomi`;
/*!50001 DROP VIEW IF EXISTS `VistaNomi`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `VistaNomi` AS SELECT 
 1 AS `name`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `VistaNomi`
--

/*!50001 DROP VIEW IF EXISTS `VistaNomi`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`test`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `VistaNomi` AS select `User`.`name` AS `name` from `User` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-29 18:31:42

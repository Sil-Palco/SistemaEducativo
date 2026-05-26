create database db_springboot_schoolmanagment;


-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: db_springboot_schoolmanagment
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Dumping data for table `apoderados`
--

LOCK TABLES `apoderados` WRITE;
/*!40000 ALTER TABLE `apoderados` DISABLE KEYS */;
INSERT INTO `apoderados` VALUES (1,'3123123','12312312','444444444','44433333','123123'),(2,'Carla','Carla','983727362','70267171','Carla');
/*!40000 ALTER TABLE `apoderados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `asisgnaciones`
--

LOCK TABLES `asisgnaciones` WRITE;
/*!40000 ALTER TABLE `asisgnaciones` DISABLE KEYS */;
INSERT INTO `asisgnaciones` VALUES (1,_binary '','Prueba','2023-08-08','2023-08-11 18:00:00.000000','2023-08-08 16:00:00.000000','Prueba',2);
/*!40000 ALTER TABLE `asisgnaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `asistencias`
--

LOCK TABLES `asistencias` WRITE;
/*!40000 ALTER TABLE `asistencias` DISABLE KEYS */;
INSERT INTO `asistencias` VALUES (1,'PUNTUAL','8/08/2023',1),(2,'PUNTUAL','8/08/2023',2);
/*!40000 ALTER TABLE `asistencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `aulas`
--

LOCK TABLES `aulas` WRITE;
/*!40000 ALTER TABLE `aulas` DISABLE KEYS */;
INSERT INTO `aulas` VALUES (1,2,'Primero','A',1,2,1);
/*!40000 ALTER TABLE `aulas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `clases`
--

LOCK TABLES `clases` WRITE;
/*!40000 ALTER TABLE `clases` DISABLE KEYS */;
INSERT INTO `clases` VALUES (1,1,1,1),(2,1,2,3);
/*!40000 ALTER TABLE `clases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (1,'Matemática'),(2,'Comunicación');
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `dias`
--

LOCK TABLES `dias` WRITE;
/*!40000 ALTER TABLE `dias` DISABLE KEYS */;
INSERT INTO `dias` VALUES (1,'LUNES'),(2,'MARTES'),(3,'MIÉRCOLES'),(4,'JUEVES'),(5,'VIERNES');
/*!40000 ALTER TABLE `dias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'ALCANTARA','ARIAS','999999999',NULL,'70267159','CALLE LAS ROSAS DEL CIELO','1994-08-18','CARLOS','MASCULINO',1),(2,'ALCANTARA','ARIAS','999999999',NULL,'70267152','CALLE LAS ROSAS DEL CIELO','1994-08-18','CARLOS','MASCULINO',1),(3,'123123','Prueba','123123123','prueba.prueba@elamericano.edu.pe','12312312','123123123123','2012-01-01','Prueba','MASCULINO',5);
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `especialidades`
--

LOCK TABLES `especialidades` WRITE;
/*!40000 ALTER TABLE `especialidades` DISABLE KEYS */;
INSERT INTO `especialidades` VALUES (1,'mATEmatica');
/*!40000 ALTER TABLE `especialidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `estudiantes`
--

LOCK TABLES `estudiantes` WRITE;
/*!40000 ALTER TABLE `estudiantes` DISABLE KEYS */;
INSERT INTO `estudiantes` VALUES (1,'3123123','12312312','123123123','123123.12312312@elamericano.edu.pe','70707070','SAN ANDRES KM 28','2023-08-02','123123','MASCULINO',1,1,1,1,1,6),(2,'Sánchez','Bengo','987654322','ander.bengo@elamericano.edu.pe','71717171','SAN ANDRES KM 28','2023-08-04','Ander','MASCULINO',2,1,1,1,1,7);
/*!40000 ALTER TABLE `estudiantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `frecuencias`
--

LOCK TABLES `frecuencias` WRITE;
/*!40000 ALTER TABLE `frecuencias` DISABLE KEYS */;
INSERT INTO `frecuencias` VALUES (1,'09:00 AM','07:00 AM',1,1),(2,'11:00 AM','08:00 AM',1,2),(3,'14:00 PM','09:00 AM',1,3),(4,'12:00 AM','10:00 AM',2,1);
/*!40000 ALTER TABLE `frecuencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `grados`
--

LOCK TABLES `grados` WRITE;
/*!40000 ALTER TABLE `grados` DISABLE KEYS */;
INSERT INTO `grados` VALUES (1,'1ER GRADO'),(2,'2DO GRADO'),(3,'3ER GRADO'),(4,'4TO GRADO');
/*!40000 ALTER TABLE `grados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
/*!40000 ALTER TABLE `materiales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `matriculas`
--

LOCK TABLES `matriculas` WRITE;
/*!40000 ALTER TABLE `matriculas` DISABLE KEYS */;
INSERT INTO `matriculas` VALUES (1,'2222','123123','1231233123123123123',1),(2,'1231','2312312','3123131231231231',2);
/*!40000 ALTER TABLE `matriculas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `niveles`
--

LOCK TABLES `niveles` WRITE;
/*!40000 ALTER TABLE `niveles` DISABLE KEYS */;
INSERT INTO `niveles` VALUES (1,'INICIAL'),(2,'PRIMARIA'),(3,'SECUNDARIA');
/*!40000 ALTER TABLE `niveles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `notas`
--

LOCK TABLES `notas` WRITE;
/*!40000 ALTER TABLE `notas` DISABLE KEYS */;
INSERT INTO `notas` VALUES (1,14,0,0,0,NULL,2,1),(2,10,0,0,0,NULL,2,2);
/*!40000 ALTER TABLE `notas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `profesor_especialidad`
--

LOCK TABLES `profesor_especialidad` WRITE;
/*!40000 ALTER TABLE `profesor_especialidad` DISABLE KEYS */;
INSERT INTO `profesor_especialidad` VALUES (3,1);
/*!40000 ALTER TABLE `profesor_especialidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `respuestas`
--

LOCK TABLES `respuestas` WRITE;
/*!40000 ALTER TABLE `respuestas` DISABLE KEYS */;
/*!40000 ALTER TABLE `respuestas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_PROFESOR'),(3,'ROLE_ESTUDIANTE');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `turnos`
--

LOCK TABLES `turnos` WRITE;
/*!40000 ALTER TABLE `turnos` DISABLE KEYS */;
INSERT INTO `turnos` VALUES (1,'MAÑANA'),(2,'TARDE');
/*!40000 ALTER TABLE `turnos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usuario_rol`
--

LOCK TABLES `usuario_rol` WRITE;
/*!40000 ALTER TABLE `usuario_rol` DISABLE KEYS */;
INSERT INTO `usuario_rol` VALUES (1,1),(2,1),(5,2),(6,3),(7,3);
/*!40000 ALTER TABLE `usuario_rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,_binary '','$2a$10$xKxJFUx0AOtzK7VjUMAC8.d8cxAa0KRBYAggeUfoZJMYT1w194/GC','70267159'),(2,_binary '','$2a$10$xKxJFUx0AOtzK7VjUMAC8.d8cxAa0KRBYAggeUfoZJMYT1w194/GC','70267152'),(5,_binary '','$2a$10$rQwEwK3pkyiEXbTnvzq9/uqasgM.I2QT5EB0SBDarKkzZmdV179mq','12312312'),(6,_binary '','$2a$10$eR41WzCSXmsD2S6WwBr3Z.sj7ukBBYy2nyrN83Mox6VG.w72Zg0qi','70707070'),(7,_binary '','$2a$10$20n7NHrSF9NE9YbvM.Elf.p9IYjjvIGbny7h3iSY0/QyqjzZqytNu','71717171');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'db_springboot_schoolmanagment'
--

--
-- Dumping routines for database 'db_springboot_schoolmanagment'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-08 20:04:53

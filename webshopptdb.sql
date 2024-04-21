-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 21, 2024 at 07:33 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `webshopptdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `USER_ID` int(11) NOT NULL,
  `FIRST_NAME` text DEFAULT NULL,
  `LAST_NAME` text DEFAULT NULL,
  `PRIMARY_ADDRESS` text DEFAULT NULL,
  `SECONDARY_ADDRESS` text DEFAULT NULL,
  `CITY` varchar(128) DEFAULT NULL,
  `POSTAL_CODE` varchar(128) DEFAULT NULL,
  `BIRTH_DATE` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`USER_ID`, `FIRST_NAME`, `LAST_NAME`, `PRIMARY_ADDRESS`, `SECONDARY_ADDRESS`, `CITY`, `POSTAL_CODE`, `BIRTH_DATE`) VALUES
(5, 'Karol', 'Rrrrrrrrr', 'Gatvės g. 88', 'Nėra', 'Vilnius', 'LT-78787', '1991-01-30'),
(8, 'Petras', 'Petrovičius', 'Petro gatvė g. 5', 'Vilniaus g. 77', 'Klaipėda', 'LT-7894', '2014-04-01');

-- --------------------------------------------------------

--
-- Table structure for table `cards`
--

CREATE TABLE `cards` (
  `USER_ID` int(11) NOT NULL,
  `CARD_NUMBER` varchar(16) DEFAULT NULL,
  `FIRST_NAME` varchar(4096) DEFAULT NULL,
  `LAST_NAME` varchar(4096) DEFAULT NULL,
  `CVC` varchar(3) DEFAULT NULL,
  `EXPIRATION_DATE` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cards`
--

INSERT INTO `cards` (`USER_ID`, `CARD_NUMBER`, `FIRST_NAME`, `LAST_NAME`, `CVC`, `EXPIRATION_DATE`) VALUES
(8, '4797488944248992', 'Karolis', 'Cinkevičius', '829', '2026-04-30');

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `CART_ID` int(11) NOT NULL,
  `CUSTOMER_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`CART_ID`, `CUSTOMER_ID`) VALUES
(1, 5);

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `COMMENT_ID` int(11) NOT NULL,
  `PARENT_ID` int(11) DEFAULT 0,
  `PRODUCT_ID` int(11) DEFAULT NULL,
  `USER_ID` int(11) NOT NULL,
  `CHAT_ID` int(11) DEFAULT NULL,
  `RATING` int(11) DEFAULT NULL,
  `TITLE` varchar(4096) DEFAULT NULL,
  `BODY` text DEFAULT NULL,
  `DATE` date DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`COMMENT_ID`, `PARENT_ID`, `PRODUCT_ID`, `USER_ID`, `CHAT_ID`, `RATING`, `TITLE`, `BODY`, `DATE`) VALUES
(1, 0, 1, 5, NULL, 2, 'This is title', 'Hello, this product sucks.', '2024-04-05'),
(2, 0, 1, 5, NULL, 0, 'Hello, wthat', 'WHat', '2024-04-05'),
(3, 0, 1, 5, NULL, 0, 'New Comment', 'What the hell is going on?', '2024-04-05'),
(4, 0, 1, 5, NULL, 0, 'rgnjwrkjnfffe', 'fefefefe', '2024-04-06'),
(5, 0, 1, 5, NULL, 3, 'asscavdv', 'vsdvsdvsdvsdvsdvsdvsd', '2024-04-15'),
(6, 0, NULL, 5, 2, NULL, 'I have a question?', 'My question is, how do I leave a question?', '2024-04-15'),
(8, 0, NULL, 5, 2, NULL, 'Hello', 'Lol, what is hgoing on?', '2024-04-15'),
(14, 7, NULL, 5, 2, NULL, 'reply to reply', 'Testing this reply to teply', '2024-04-17'),
(15, 0, NULL, 5, 2, NULL, 'TOPLEVELCOMMENT', 'commenttext', '2024-04-17'),
(16, 7, NULL, 5, 2, NULL, 'testing', 'amamflamlskfma', '2024-04-17'),
(18, 0, 6, 6, NULL, 5, 'This is title', 'Hello, such an awesome product', '2024-04-06'),
(24, 6, NULL, 5, 2, NULL, 'lvl2', 'reply to lvl1', '2024-04-20'),
(25, 24, NULL, 5, 2, NULL, 'lvl3', 'reply to lvl2', '2024-04-20'),
(26, 1, NULL, 5, 0, NULL, 'reply to a rating', 'reply to rating', '2024-04-20'),
(28, 1, NULL, 5, 0, NULL, 'replying', 'reply', '2024-04-21');

-- --------------------------------------------------------

--
-- Table structure for table `managers`
--

CREATE TABLE `managers` (
  `USER_ID` int(11) DEFAULT NULL,
  `FIRST_NAME` varchar(256) DEFAULT NULL,
  `LAST_NAME` varchar(256) DEFAULT NULL,
  `ASSIGNED_ORDER_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `ORDER_ID` int(11) NOT NULL,
  `CUSTOMER_ID` int(11) DEFAULT NULL,
  `CART_ID` int(11) DEFAULT NULL,
  `ASSIGNED_MANAGER_ID` int(11) NOT NULL DEFAULT 0,
  `ORDER_DATE` date NOT NULL DEFAULT current_timestamp(),
  `ORDER_STATUS` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`ORDER_ID`, `CUSTOMER_ID`, `CART_ID`, `ASSIGNED_MANAGER_ID`, `ORDER_DATE`, `ORDER_STATUS`) VALUES
(2, 5, 1, 0, '2024-04-12', 0),
(3, 5, 1, 0, '2024-04-15', 0),
(4, 5, 1, 0, '2024-04-15', 0),
(5, 5, 1, 9, '2024-04-15', 2),
(6, 5, 1, 9, '2024-04-15', 2),
(15, NULL, 1, 0, '2024-04-18', 0);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `PRODUCT_ID` int(11) NOT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `AVERAGE_RATING` float DEFAULT NULL,
  `PRICE` float DEFAULT NULL,
  `NAME` varchar(256) DEFAULT NULL,
  `BRAND` varchar(256) DEFAULT NULL,
  `DESCRIPTION` text DEFAULT NULL,
  `CATEGORY` varchar(256) DEFAULT NULL,
  `PRODUCT_TYPE` varchar(256) DEFAULT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `COMPOSITION` longtext DEFAULT NULL,
  `TYPE` varchar(256) DEFAULT NULL,
  `COLOR` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`PRODUCT_ID`, `QUANTITY`, `AVERAGE_RATING`, `PRICE`, `NAME`, `BRAND`, `DESCRIPTION`, `CATEGORY`, `PRODUCT_TYPE`, `CAPACITY`, `COMPOSITION`, `TYPE`, `COLOR`) VALUES
(1, 417, 1, 6.19, 'COLOR VIVE, šampūnas dažytiems plaukams', 'ELVITAL', 'Jūsų plaukai dažyti viena spalva arba sruogelėmis? Puoselėjamasis šampūnas su raudonaisiais bijūnais ir UV filtrais maitina, gaivina ir saugo dažytus plaukus iki 10 savaičių.', 'Shampoo', 'Liquid', 400, 'Aqua/Water, Sodium Laureth Sulfate, Dimethicone, Coco-Betaine, Sodium Chloride, Glycol Distearate, Guar Hydroxypropyltrimonium Chloride, Cocamide Mipa', 'Plaukų priežiūra', NULL),
(2, 198, 0, 16.99, 'GARNIER BOTANIC THERAPY, RICIN & ALMOND, plaukų šampūnas', 'GARNIER', 'Stiprinamasis ricinų aliejus ir maitinamasis migdolų aliejus sumaišyti unikalioje produkto formulėje, skirtoje silpniems, linkusiems slinkti plaukams.', 'Shampoo', 'Liquid', 400, 'Aqua/Water, Sodium Laureth Sulfate, Cocamidopropyl Betaine, Sodium Lauryl Sulfate, Glycerin, Glycol Distearate, Amodimethicone, Sodium Chloride, CI 19140/Yellow 5, CI 14700/Red 4, Guar Hydroxypropyltrimonium Chloride, Coco-Betaine, Ricinus Communis Oil/Cas', 'Plaukų priežiūra', NULL),
(13, 4, 0, 8.99, 'UpdatedName', 'Brand', 'Desc', 'Cat', 'Liquid', 750, 'Comp', 'Type', NULL),
(16, 12, 0, 12.5, 'name', 'fsdfdsf', 'sdfsdfds', 'fdsfdsf', 'Hairdye', 500, 'anfsanfkjanfjkansjnkjnf', 'asfsadasd', 'anfsanfkjanfjkansjnkjnf');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `USER_ID` int(11) NOT NULL,
  `EMAIL` varchar(256) DEFAULT NULL,
  `PASSWORD` longtext DEFAULT NULL,
  `NAME` varchar(256) DEFAULT NULL,
  `SURNAME` varchar(256) DEFAULT NULL,
  `ACCOUNT_TYPE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`USER_ID`, `EMAIL`, `PASSWORD`, `NAME`, `SURNAME`, `ACCOUNT_TYPE`) VALUES
(4, 'elvin@hotmail.lt', '1000:711bce8f8d6b4feab157e7f4eff5f849:7255326af7219e178da97fd489d707df998376b14bdfbea0e75d38530875f84469d64c2dbbf89462b7a9b6bcd493abba6cc15fed78002cb5ccaba0099a7e9705', 'Elvinsky', 'Zhukovsky', 0),
(5, 'karolisrr7@gmail.com', '1000:bf45bd773e093c14b1810e905c49900a:690cda58bad2805c45b38f2b22fc5c175e6501b1aac6976fc97c708640416487122a411b7f5c42a53cf72c46713148b6ecd3a603a5ec28665759e223079b68b9', 'Karka', 'Sarka', 2),
(8, 'customer@yahoo.com', '1000:8a899c2b48b52fa24c72bd1eabfff556:73859725a7d195c2c4fcf171a879be2a36994076e06c68e649751e4690891ca2475e1c3b45ee4a7846c6fc83b7abb7df77a5dccd20d72ba49fa360f5eac9bab8', 'Customername', 'Customersurname', 0),
(9, 'manager@webshoppt.com', '1000:04a369799ae98d03603c424abdb7ff5d:109f6dddfb875d609584db3e4925d48d0f1300cbe4b6d28c578eb83c049131c4fde0b0bb812b690d4ba30dd464d70134afa97fda0dbd6a2cc8327755d51ca4c3', 'Managername', 'Managersurname', 1),
(11, 'testas@email.com', '1000:3e7fd0425fd5c773ee4e0a29db94e9cd:70dea14ecb547452e3356febf73d7ad9abde4c4811d3f6983dfd63e315e5e02003a197e1719e30b049bd4bf3f633e8e6d3aa37612fc56e5cd54e464d1d0455db', 'Testas', 'Testukas', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`USER_ID`),
  ADD UNIQUE KEY `USER_ID` (`USER_ID`);

--
-- Indexes for table `cards`
--
ALTER TABLE `cards`
  ADD PRIMARY KEY (`USER_ID`),
  ADD UNIQUE KEY `USER_ID` (`USER_ID`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`CART_ID`),
  ADD UNIQUE KEY `CUSTOMER_ID` (`CUSTOMER_ID`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`COMMENT_ID`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`ORDER_ID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`PRODUCT_ID`),
  ADD UNIQUE KEY `PRODUCT_ID` (`PRODUCT_ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`USER_ID`),
  ADD UNIQUE KEY `USER_ID` (`USER_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `CART_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `COMMENT_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `ORDER_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `PRODUCT_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `USER_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

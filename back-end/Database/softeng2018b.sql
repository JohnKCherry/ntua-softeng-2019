-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 29, 2018 at 02:08 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `softeng2018b`
--

-- --------------------------------------------------------

--
-- Table structure for table `favourites`
--

CREATE TABLE `favourites` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `favourites`
--

INSERT INTO `favourites` (`user_id`, `product_id`) VALUES
(3, 4);

-- --------------------------------------------------------

--
-- Table structure for table `prices`
--

CREATE TABLE `prices` (
  `product_id` int(11) NOT NULL,
  `shop_id` int(11) NOT NULL,
  `price` decimal(11,0) NOT NULL,
  `dateFrom` date NOT NULL,
  `dateTo` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `prices`
--

INSERT INTO `prices` (`product_id`, `shop_id`, `price`, `dateFrom`, `dateTo`) VALUES
(2, 1, '1500', '2018-12-29', NULL),
(4, 2, '450', '2018-12-29', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` mediumtext,
  `category` varchar(128) NOT NULL,
  `withdrawn` bit(1) NOT NULL DEFAULT b'0',
  `tags` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `description`, `category`, `withdrawn`, `tags`) VALUES
(1, 'Vendor TV 32\'\' Model X', 'Mplah mplah', 'TV', b'0', 'TV, Smart TV'),
(2, 'OtherVendor TV 42\'\' Model Y', 'Mplah Mplah', 'TV', b'0', 'TV, Smart TV'),
(4, 'Sony PS4 Pro', 'Very expensive game console', 'Gaming \r\n', b'0', '4K\r\nPS4');

-- --------------------------------------------------------

--
-- Table structure for table `shops`
--

CREATE TABLE `shops` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `address` varchar(255) COLLATE utf8_bin NOT NULL,
  `location` point NOT NULL,
  `tags` text COLLATE utf8_bin,
  `withdrawn` bit(1) NOT NULL DEFAULT b'0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `shops`
--

INSERT INTO `shops` (`id`, `name`, `address`, `location`, `tags`, `withdrawn`) VALUES
(1, 'Πλαίσιο Πειραιά', 'Καραϊσκάκη 32', '\0\0\0\0\0\0\0��x�Z�7@��$��B@', 'Εύκολη πρόσβαση', b'0'),
(2, 'Κωτσόβολος Κερατσινίου', 'Λεωφόρος Δημοκρατίας 34', '�\0\0\0\0\0k�#�]�7@��Ӝ��B@', 'Γρήγορη εξυπηρέτηση', b'0');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `password` varchar(50) COLLATE utf8_bin NOT NULL,
  `authorization` int(1) NOT NULL DEFAULT '1',
  `email` varchar(255) COLLATE utf8_bin NOT NULL,
  `fullname` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `authorization`, `email`, `fullname`) VALUES
(1, 'gkef1996', 'asfalhskwdikos', 3, 'g.kef@hotmail.com', 'Γιώργος Κεφάλας'),
(2, 'johndoe', 'eukoloskwdikos', 1, 'donotreply@john.doe', 'John Doe'),
(3, 'master_Roshi', 'Kamehouse', 2, 'master_roshi@dragonball.com', 'Master Roshi'),
(4, 'ethelontarathskardiasmas', 'zwgianauphretw', 2, 'charity@gmail.com', 'Mike Green');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `favourites`
--
ALTER TABLE `favourites`
  ADD PRIMARY KEY (`user_id`,`product_id`),
  ADD KEY `favourites_user_id_index` (`user_id`),
  ADD KEY `favourites_product_id_index` (`product_id`);

--
-- Indexes for table `prices`
--
ALTER TABLE `prices`
  ADD PRIMARY KEY (`product_id`,`shop_id`),
  ADD UNIQUE KEY `product_id` (`product_id`),
  ADD UNIQUE KEY `shop_id` (`shop_id`),
  ADD KEY `prices_product_id_index` (`product_id`),
  ADD KEY `prices_shop_id_index` (`shop_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `shops`
--
ALTER TABLE `shops`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `shop_names` (`name`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `shop_locations` (`location`(25));

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username_index` (`username`),
  ADD UNIQUE KEY `users_email_index` (`email`),
  ADD KEY `users_fullname_index` (`fullname`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `shops`
--
ALTER TABLE `shops`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `favourites`
--
ALTER TABLE `favourites`
  ADD CONSTRAINT `favourites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `favourites_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `prices`
--
ALTER TABLE `prices`
  ADD CONSTRAINT `prices_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `prices_ibfk_2` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

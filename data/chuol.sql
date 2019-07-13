-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jul 13, 2019 at 04:01 AM
-- Server version: 5.7.24
-- PHP Version: 7.2.14

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chuol`
--

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
CREATE TABLE IF NOT EXISTS `cities` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`id`, `name`) VALUES
(1, 'Siem Reap'),
(2, 'Phnom Penh'),
(3, 'Kandal');

-- --------------------------------------------------------

--
-- Table structure for table `estates`
--

DROP TABLE IF EXISTS `estates`;
CREATE TABLE IF NOT EXISTS `estates` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `price` double(8,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `phone` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `accepted` tinyint(1) NOT NULL,
  `publish` tinyint(1) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `duration` varchar(255) NOT NULL,
  `phone_option` int(11) DEFAULT NULL,
  `city_id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `estates_user_id_foreign` (`user_id`),
  KEY `estates_city_id_foreign` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `estates`
--

INSERT INTO `estates` (`id`, `title`, `price`, `description`, `phone`, `address`, `date`, `lat`, `lng`, `accepted`, `publish`, `currency`, `duration`, `phone_option`, `city_id`, `user_id`) VALUES
(1, 'House near school', 200000.00, 'The house is awsome', '077686535', 'Preah monivong,sangkat srah chork', '2019-06-20', 0, 0, 1, 1, 'Dollar', '15', NULL, 1, 1),
(2, 'Urgent house', 200000.00, 'The house is awsome', '077686535', 'Preah monivong,sangkat srah chork', '2019-06-20', 11.5894, 104.8959, 1, 1, 'Dollar', '15', NULL, 2, 1),
(3, 'Room good for student', 60.00, 'The room is awsome', '012335625', 'Preah monivong,sangkat srah chork', '2019-06-20', 11.605947640145859, 104.89679507911204, 1, 1, 'Dollar', '15', NULL, 2, 1),
(4, 'Cool house', 300.00, 'The house is awsome', '077856535', 'Preah monivong,sangkat srah chork', '2019-06-20', 11.58436, 104.93021, 1, 1, 'Dollar', '15', NULL, 1, 1),
(5, 'Cool house', 1000.00, 'the house is cool', '08796646', 'simewhere', '2019-07-10', 0, 0, 1, 1, 'Dollar', '15', NULL, 1, 1),
(6, 'Nice house', 87.98, 'the house is nice', '085557854', 'somewhere', '2019-07-10', 0, 0, 1, 1, 'Dollar', '15', NULL, 1, 1),
(7, 'My room', 60.00, 'This is my room', '07764866', 'somewhere', '2019-07-10', 0, 0, 1, 1, 'Dollar', '15', NULL, 1, 1),
(8, 'Hi', 54695.00, 'hi', '09979761', 'soneno', '2019-07-10', 0, 0, 1, 1, 'Dollar', '15', NULL, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `estate_imgs`
--

DROP TABLE IF EXISTS `estate_imgs`;
CREATE TABLE IF NOT EXISTS `estate_imgs` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `img_loc` varchar(255) NOT NULL,
  `estate_id` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `estate_imgs_estate_id_foreign` (`estate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `estate_imgs`
--

INSERT INTO `estate_imgs` (`id`, `img_loc`, `estate_id`) VALUES
(1, 'rs_1024x759-180724102221-1024-House_1560994253.jpg', 1),
(2, '1528256659294_1560994253.jpg', 1),
(3, 'photo-1523217582562-09d0def993a6_1560994253.jpeg', 1),
(4, 'pexels-photo-186077_1560994319.jpeg', 2),
(5, 'Malan-Vorster-Clifton-low-res-85-002_1560994319.jpg', 2),
(6, 'Perfect-Hideaways-Casa-Blanca-Clifton-95_1560994319.jpg', 2),
(7, 'Rent_room_stockholm_1560995132.jpg', 3),
(8, '18473835_1560995132.jpg', 3),
(9, 'White-room-feature_1560995132.jpg', 3),
(10, 'MHA.JR_201708_038_1560995587.jpg', 4),
(11, 'clifton-house-project-architecture_dezeen_hero-1_1560995587.jpg', 4),
(12, 'Perfect-Hideaways-Casa-Blanca-Clifton-90_1560995587.jpg', 4),
(13, '153095940_1562774643.jpg', 5),
(14, 'MHA.JR_201708_038_1562774644.jpg', 5),
(15, '5d17d2ac-165c8f93378_1562774644.jpg', 5),
(16, 'City-2Queen-beds-window-NEW-wide_1562774644.jpg', 5),
(17, 'MHA.JR_201708_038_1562774995.jpg', 6),
(18, 'Perfect-Hideaways-Casa-Blanca-Clifton-90_1562774995.jpg', 6),
(19, 'pexels-photo-186077_1562774995.jpeg', 6),
(20, 'White-room-feature_1562775510.jpg', 7),
(21, '18473835_1562775510.jpg', 7),
(22, 'Rent+room+stockholm_1562775510.jpg', 7),
(23, 'rs_1024x759-180724102221-1024-House_1562776376.jpg', 8),
(24, 'details_820x461_IMG_8606-HDR_1562776376.jpg', 8),
(25, '1528256659294_1562776376.jpg', 8);

-- --------------------------------------------------------

--
-- Table structure for table `houses`
--

DROP TABLE IF EXISTS `houses`;
CREATE TABLE IF NOT EXISTS `houses` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bedroom` tinyint(3) UNSIGNED NOT NULL,
  `bathroom` tinyint(3) UNSIGNED NOT NULL,
  `floor` int(11) NOT NULL,
  `house_size` varchar(255) NOT NULL,
  `yard_size` varchar(255) NOT NULL,
  `for_sale_status` varchar(255) NOT NULL,
  `estate_id` int(10) UNSIGNED NOT NULL,
  `house_type_id` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `houses_estate_id_foreign` (`estate_id`),
  KEY `houses_house_type_id_foreign` (`house_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `houses`
--

INSERT INTO `houses` (`id`, `bedroom`, `bathroom`, `floor`, `house_size`, `yard_size`, `for_sale_status`, `estate_id`, `house_type_id`) VALUES
(1, 4, 4, 2, '10 x 10', '10 x 11', 'For Sale', 1, 3),
(2, 4, 4, 2, '10 x 10', '10 x 11', 'For Rent', 2, 2),
(3, 4, 4, 2, '15 x 13', '16 x 18', 'For Rent', 4, 3),
(4, 5, 5, 5, '10 x 11', '10 x 15', 'For Rent', 5, 3),
(5, 7, 6, 3, '10 x 10', '10 x 10', 'For Sale', 6, 3),
(6, 5, 5, 3, '10', '15', 'For Sale', 8, 3);

-- --------------------------------------------------------

--
-- Table structure for table `house_types`
--

DROP TABLE IF EXISTS `house_types`;
CREATE TABLE IF NOT EXISTS `house_types` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `house_types`
--

INSERT INTO `house_types` (`id`, `name`) VALUES
(1, 'Castle'),
(2, 'Caravan'),
(3, 'Bungalow'),
(4, 'Maison'),
(5, 'Villa');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) NOT NULL,
  `date` date NOT NULL,
  `seen` tinyint(1) NOT NULL,
  `from_self_user_id` int(10) UNSIGNED NOT NULL,
  `to_user_user_id` int(10) UNSIGNED NOT NULL,
  `estate_id` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `messages_from_self_user_id_foreign` (`from_self_user_id`),
  KEY `messages_to_user_user_id_foreign` (`to_user_user_id`),
  KEY `messages_estate_id_foreign` (`estate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `content`, `date`, `seen`, `from_self_user_id`, `to_user_user_id`, `estate_id`) VALUES
(1, 'Hi', '2019-06-20', 0, 2, 1, 1),
(2, 'hello', '2019-06-20', 0, 1, 2, 1),
(3, 'hi', '2019-07-10', 0, 1, 1, 2),
(4, 'hi', '2019-07-10', 0, 1, 1, 2),
(5, 'hi', '2019-07-11', 0, 1, 2, 1),
(6, 'sometimes the message can\'t be send', '2019-07-11', 0, 1, 1, 2),
(7, 'sometimes the message can\'t be send', '2019-07-11', 0, 1, 1, 2),
(8, 'sometimes the message can\'t be send', '2019-07-11', 0, 1, 1, 2),
(9, 'sometimes it the method is call many time creating duplicate msg....', '2019-07-11', 0, 1, 1, 2),
(10, 'sometimes it the method is call many time creating duplicate msg....', '2019-07-11', 0, 1, 1, 2),
(11, 'sometimes it the method is call many time creating duplicate msg....', '2019-07-11', 0, 1, 1, 2),
(12, 'sometimes it the method is call many time creating duplicate msg....', '2019-07-11', 0, 1, 1, 2),
(13, 'shit', '2019-07-11', 0, 1, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
CREATE TABLE IF NOT EXISTS `rooms` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `size` varchar(255) NOT NULL,
  `free_wifi` tinyint(1) NOT NULL,
  `parking_space_avalible` tinyint(1) NOT NULL,
  `AC` tinyint(1) NOT NULL,
  `estate_id` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rooms_estate_id_foreign` (`estate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `size`, `free_wifi`, `parking_space_avalible`, `AC`, `estate_id`) VALUES
(1, '2 x 2', 1, 1, 0, 3),
(2, '2 x 2', 1, 1, 0, 7);

-- --------------------------------------------------------

--
-- Table structure for table `saved_post`
--

DROP TABLE IF EXISTS `saved_post`;
CREATE TABLE IF NOT EXISTS `saved_post` (
  `user_id` int(10) UNSIGNED NOT NULL,
  `estate_id` int(10) UNSIGNED NOT NULL,
  KEY `saved_post_user_id_foreign` (`user_id`),
  KEY `saved_post_estate_id_foreign` (`estate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `fname` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `remember_token` varchar(100) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `img_loc` varchar(255) NOT NULL DEFAULT 'default_user.png',
  `user_type_id` int(10) UNSIGNED NOT NULL DEFAULT '2',
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`),
  KEY `users_user_type_id_foreign` (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `fname`, `lname`, `phone`, `password`, `remember_token`, `email`, `img_loc`, `user_type_id`) VALUES
(1, NULL, 'Aun', 'Chounry', '077623562', '$2y$10$pbmhfO2ieijlqOybRE5QAu7m7uC9I.NRD3WyDdwkvARwqK.hh4R3O', NULL, 'oun.chounry.kh@gmail.com', 'default_user.png', 2),
(2, NULL, 'Zee', 'Bra', '077658535', '$2y$10$QLomhCQsPmmNgMgVzvMrHOojsIgne5G5gSWrSUs4HN9NxX3RTZ4fC', NULL, 'zee@gmail.com', 'default_user.png', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_types`
--

DROP TABLE IF EXISTS `user_types`;
CREATE TABLE IF NOT EXISTS `user_types` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_types`
--

INSERT INTO `user_types` (`id`, `name`) VALUES
(1, 'Admin'),
(2, 'Member');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `estates`
--
ALTER TABLE `estates`
  ADD CONSTRAINT `estates_city_id_foreign` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
  ADD CONSTRAINT `estates_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `estate_imgs`
--
ALTER TABLE `estate_imgs`
  ADD CONSTRAINT `estate_imgs_estate_id_foreign` FOREIGN KEY (`estate_id`) REFERENCES `estates` (`id`);

--
-- Constraints for table `houses`
--
ALTER TABLE `houses`
  ADD CONSTRAINT `houses_estate_id_foreign` FOREIGN KEY (`estate_id`) REFERENCES `estates` (`id`),
  ADD CONSTRAINT `houses_house_type_id_foreign` FOREIGN KEY (`house_type_id`) REFERENCES `house_types` (`id`);

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_estate_id_foreign` FOREIGN KEY (`estate_id`) REFERENCES `estates` (`id`),
  ADD CONSTRAINT `messages_from_self_user_id_foreign` FOREIGN KEY (`from_self_user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `messages_to_user_user_id_foreign` FOREIGN KEY (`to_user_user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `rooms`
--
ALTER TABLE `rooms`
  ADD CONSTRAINT `rooms_estate_id_foreign` FOREIGN KEY (`estate_id`) REFERENCES `estates` (`id`);

--
-- Constraints for table `saved_post`
--
ALTER TABLE `saved_post`
  ADD CONSTRAINT `saved_post_estate_id_foreign` FOREIGN KEY (`estate_id`) REFERENCES `estates` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `saved_post_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_user_type_id_foreign` FOREIGN KEY (`user_type_id`) REFERENCES `user_types` (`id`);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

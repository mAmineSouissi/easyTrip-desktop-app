-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 06, 2025 at 05:37 AM
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
-- Database: `easyTrip`
--

-- --------------------------------------------------------

--
-- Table structure for table `agency`
--

CREATE TABLE `agency` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `agency`
--

INSERT INTO `agency` (`id`, `name`, `address`, `phone`, `email`, `image`, `user_id`) VALUES
(1, 'Agence1', 'tounes', '55448878', 'aminesouissi681@gmail.com', 'file:/home/cardinal/Downloads/defaultPic.jpg', 2),
(2, 'Agence2', 'asds', '25649878', 'aminesouissi682@gmail.com', 'file:/home/cardinal/Downloads/ticket.jpg', 1),
(3, 'agence3', 'ariana,tunis', '25664987', 'oussamabani14@gmail.com', 'file:/home/cardinal/Downloads/istockphoto-119926339-612x612.jpg', 2),
(4, 'f', 'f', '12345678', 'youssefcarma@gmail.com', 'file:/home/cardinal/Downloads/hotel2.jpg', 1);

-- --------------------------------------------------------

--
-- Table structure for table `cars`
--

CREATE TABLE `cars` (
  `id` int(11) NOT NULL,
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `seats` int(11) NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price_per_day` float NOT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `availability` enum('AVAILABLE','NOT_AVAILABLE') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `cars`
--

INSERT INTO `cars` (`id`, `model`, `seats`, `location`, `price_per_day`, `image`, `availability`) VALUES
(15, 'golf8', 5, 'italy', 10, 'C:\\Users\\HP\\Downloads\\JDBC-main\\JDBC-main\\src\\main\\resources\\images\\Golf_GTE-1.png', 'AVAILABLE'),
(17, 'golf4', 1, 'MgMaxiAriana', 17, 'C:\\Users\\HP\\Downloads\\JDBC-main\\JDBC-main\\src\\main\\resources\\images\\252841.png', 'AVAILABLE'),
(21, 'BMW serie 8', 2, 'Monaco', 140, 'C:\\Users\\HP\\Downloads\\JDBC-main\\JDBC-main\\src\\main\\resources\\images\\bmw-8series-coupe-modellfinder.png', 'AVAILABLE'),
(22, 'toktok', 3, 'India', 5, 'C:\\Users\\HP\\Downloads\\JDBC-main\\JDBC-main\\src\\main\\resources\\images\\toktok.png', 'AVAILABLE'),
(23, 'Bugati Shiron', 2, 'DubaiMall', 2100, 'C:\\Users\\HP\\Downloads\\JDBC-main\\JDBC-main\\src\\main\\resources\\images\\Bugatti-Chiron-Car.png', 'AVAILABLE');

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `offerId` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `message` varchar(50) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`id`, `userId`, `offerId`, `rating`, `message`, `date`) VALUES
(41, 16, 1, 1, 'asdasd', '2025-03-05');

-- --------------------------------------------------------

--
-- Table structure for table `hotels`
--

CREATE TABLE `hotels` (
  `id_hotel` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `price` float DEFAULT NULL,
  `type_room` enum('Simple','Double','Suit') DEFAULT NULL,
  `num_room` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `promotion_id` int(11) DEFAULT NULL,
  `agency_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotels`
--

INSERT INTO `hotels` (`id_hotel`, `name`, `adresse`, `city`, `rating`, `description`, `price`, `type_room`, `num_room`, `image`, `promotion_id`, `agency_id`, `user_id`) VALUES
(1, 'Hotel Paradise', '123 Beach Road', 'Miami', 5, 'A luxurious beachside hotel with excellent amenities.', 150, 'Double', 50, 'http://localhost/img/hotel.jpg', 2, 5, 13),
(2, 'Hotel Paradise', '123 Beach Road', 'Miami', 5, 'A luxurious beachside hotel with excellent amenities.', 150, 'Simple', 50, 'http://localhost/img/hotel2.jpg', 1, NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `offer_travel`
--

CREATE TABLE `offer_travel` (
  `id` int(11) NOT NULL,
  `departure` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `departure_date` date DEFAULT NULL,
  `arrival_date` date DEFAULT NULL,
  `hotelName` varchar(50) NOT NULL,
  `flightName` varchar(255) DEFAULT NULL,
  `discription` varchar(255) DEFAULT NULL,
  `category` enum('RELIGION','SPORTIVE','ROMANTIQUE','TOURISTIQUE') DEFAULT NULL,
  `price` float DEFAULT NULL,
  `image` varchar(255) NOT NULL,
  `agency_id` int(11) DEFAULT NULL,
  `promotion_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `offer_travel`
--

INSERT INTO `offer_travel` (`id`, `departure`, `destination`, `departure_date`, `arrival_date`, `hotelName`, `flightName`, `discription`, `category`, `price`, `image`, `agency_id`, `promotion_id`) VALUES
(5, 'Tunis', 'Maroc', '2025-02-05', '2025-02-26', 'Robeca', 'Tunisair', 'belle description', 'TOURISTIQUE', 130, 'C:\\Users\\user\\Pictures\\Screenshots\\Capture d\'écran 2025-01-12 224849.png', 1, 7),
(12, 'Tunisia', 'Istanbul', '2025-03-08', '2025-03-09', 'Regency', 'Tunisiair', 'tunisia', 'SPORTIVE', 76.5, 'C:\\xampp\\htdocs\\img\\téléchargement.jpeg', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `Option`
--

CREATE TABLE `Option` (
  `id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `option_text` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Panier`
--

CREATE TABLE `Panier` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `reservation_id` int(11) DEFAULT NULL,
  `coupon_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `total_price` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `promotion`
--

CREATE TABLE `promotion` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `discount_percentage` float DEFAULT NULL,
  `valid_until` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promotion`
--

INSERT INTO `promotion` (`id`, `title`, `description`, `discount_percentage`, `valid_until`) VALUES
(1, 'Winter Sale', 'Get 20% off on all rooms for the winter season', 15, '2025-03-31'),
(3, 'Ramadhan', 'karim', 25, '2025-03-08');

-- --------------------------------------------------------

--
-- Table structure for table `Question`
--

CREATE TABLE `Question` (
  `id` int(11) NOT NULL,
  `survey_id` int(11) NOT NULL,
  `question_text` text NOT NULL,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reclamation`
--

CREATE TABLE `reclamation` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `status` varchar(15) NOT NULL,
  `date` date NOT NULL,
  `issue` varchar(15) NOT NULL,
  `category` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reclamation`
--

INSERT INTO `reclamation` (`id`, `userId`, `status`, `date`, `issue`, `category`) VALUES
(29, 16, 'bug', '2025-02-19', 'site', 'En cours'),
(30, 1, 'Optimisation ', '2025-02-20', 'Performance', 'Closed'),
(32, 1, 'bug', '2025-02-20', 'aa', 'En cours'),
(34, 1, 'jqsx', '2025-02-22', 'qsxqsx', 'En attente'),
(36, 1, 'LKO', '2025-02-25', 'ASDASdddd', 'En attente'),
(37, 5, 'a', '2025-02-27', 'b', 'En attente'),
(38, 5, 'asds', '2025-02-27', 'asdasd', 'En attente'),
(39, 2, 'asdasd', '2025-02-27', 'asdasda', 'En attente'),
(40, 16, 'looll', '2025-03-02', 'lool', 'En attente'),
(41, 2, 'En attente', '2025-03-05', 'issues', 'Bug');

-- --------------------------------------------------------

--
-- Table structure for table `Reservation`
--

CREATE TABLE `Reservation` (
  `id_reservation` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `travel_id` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `orderDate` date DEFAULT NULL,
  `ticket_id` int(11) DEFAULT NULL,
  `hotel_id` int(11) DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `places` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Reservation`
--

INSERT INTO `Reservation` (`id_reservation`, `user_id`, `travel_id`, `status`, `orderDate`, `ticket_id`, `hotel_id`, `nom`, `prenom`, `phone`, `email`, `places`) VALUES
(1, 16, -1, 'En attente', '2025-03-04', 1, -1, 'Amine', 'user', '12312312', 'aminesouissi682@gmail.com', '4'),
(2, 16, -1, 'En attente', '2025-03-04', 1, -1, 'Amine', 'user', '12312312', 'aminesouissi682@gmail.com', '3'),
(3, 16, -1, 'En attente', '2025-03-04', 1, -1, 'Amine', 'user', '12345123', 'aminesouissi682@gmail.com', '8');

-- --------------------------------------------------------

--
-- Table structure for table `res_transport`
--

CREATE TABLE `res_transport` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` enum('IN_PROGRESS','CANCELED','DONE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `res_transport`
--

INSERT INTO `res_transport` (`id`, `user_id`, `car_id`, `start_date`, `end_date`, `status`) VALUES
(15, 1, 15, '2025-04-06', '2025-05-09', 'IN_PROGRESS'),
(19, 1, 15, '2025-02-28', '2025-03-22', 'IN_PROGRESS'),
(20, 1, 15, '2025-03-06', '2025-03-19', 'IN_PROGRESS');

-- --------------------------------------------------------

--
-- Table structure for table `Survey`
--

CREATE TABLE `Survey` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `category` varchar(50) NOT NULL,
  `created_by` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `SurveyResponse`
--

CREATE TABLE `SurveyResponse` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `survey_id` int(11) NOT NULL,
  `response_data` text NOT NULL,
  `recommendations` text DEFAULT NULL,
  `completed_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `id_ticket` int(11) NOT NULL,
  `flight_number` int(11) DEFAULT NULL,
  `airline` varchar(255) DEFAULT NULL,
  `departure_city` varchar(255) DEFAULT NULL,
  `arrival_city` varchar(255) DEFAULT NULL,
  `departure_date` date DEFAULT NULL,
  `departure_time` time DEFAULT NULL,
  `arrival_date` date DEFAULT NULL,
  `arrival_time` time DEFAULT NULL,
  `ticket_class` enum('Economy','Business','First') DEFAULT NULL,
  `price` float DEFAULT NULL,
  `ticket_type` enum('one-way','round-trip') DEFAULT NULL,
  `image_airline` varchar(255) DEFAULT NULL,
  `city_image` varchar(1000) NOT NULL,
  `agency_id` int(11) DEFAULT NULL,
  `promotion_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`id_ticket`, `flight_number`, `airline`, `departure_city`, `arrival_city`, `departure_date`, `departure_time`, `arrival_date`, `arrival_time`, `ticket_class`, `price`, `ticket_type`, `image_airline`, `city_image`, `agency_id`, `promotion_id`, `user_id`) VALUES
(1, 123, 'Air France', 'Paris', 'New York', '2025-03-15', '08:30:00', '2025-03-15', '14:15:00', 'Economy', 750, 'one-way', 'airfrance.png', 'http://localhost/img/hotel2.jpg', 2, 1, 2),
(2, 221, 'asdasd', 'asdasda', 'ILL', '2025-02-27', '09:22:00', '2025-02-28', '12:00:00', 'Economy', 2000, 'one-way', NULL, 'http://localhost/img/ticket.jpg', 2, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `addresse` varchar(255) DEFAULT NULL,
  `profilePhoto` varchar(255) DEFAULT NULL,
  `role` enum('Admin','Agent','Client') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`id`, `name`, `surname`, `password`, `email`, `phone`, `addresse`, `profilePhoto`, `role`) VALUES
(1, 'Admin', 'Admin', '$2a$10$HbkdD6VU6DUuIRBqTKfL8OfNn2jSAODQrW2UlgNZT2k0BFzcmHSLW', 'admin@admin.com', '12345678', '123 Admin Street', 'http://localhost/img/profile/my-notion-face-portrait.png', 'Admin'),
(2, 'Agent', 'user', '$2a$10$HbkdD6VU6DUuIRBqTKfL8OfNn2jSAODQrW2UlgNZT2k0BFzcmHSLW', 'agent@agent.com', '98987656', '456 Agent Avenue', 'http://localhost/img/profile/profile3.jpg', 'Agent'),
(4, 'Client', 'User', '$2a$10$HbkdD6VU6DUuIRBqTKfL8OfNn2jSAODQrW2UlgNZT2k0BFzcmHSLW', 'client@gmail.com', '22545445', 'Client Avenue 123', 'http://localhost/img/profile/profile3.jpg', 'Client'),
(5, 'Cardinal', 'cardinal', '$2a$10$HbkdD6VU6DUuIRBqTKfL8OfNn2jSAODQrW2UlgNZT2k0BFzcmHSLW', 'home@home.com', '28619223', 'Tunisia', 'http://localhost/img/profile/profile3.jpg', 'Client'),
(13, 'cardinal', 'user', '$2a$10$HbkdD6VU6DUuIRBqTKfL8OfNn2jSAODQrW2UlgNZT2k0BFzcmHSLW', 'omsehli@gmail.com', '+21628619391', 'Tunis', 'http://localhost/img/profile/my-notion-face-portrait.png', 'Agent'),
(16, 'Amine', 'user', '$2a$10$LgwUyGPRlUGqU7DmletL7eZo2T2QfK6j9Uw9n/RVOc4/0PKxwoQD6', 'aminesouissi682@gmail.com', '+21628657499', 'CHANGE ME', 'https://lh3.googleusercontent.com/a/ACg8ocKH11yx0c0bQ0_DFLY8B96UG3mA5RotBWdV2nOwrqOHc-eeCU0=s96-c', 'Client'),
(17, 'User1', 'user', '$2a$10$PkkRCgLBjfEbpFZxZL6dT.4eVL4Gc5nO5wOqKJt/7U36QiC2nmla.', 'user@client.com', '25649879', 'tunis', 'http://localhost/img/profile/defaultPic.jpg', 'Client');

-- --------------------------------------------------------

--
-- Table structure for table `verification_codes`
--

CREATE TABLE `verification_codes` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `code` varchar(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `expires_at` timestamp NULL DEFAULT NULL,
  `used` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `webinaire`
--

CREATE TABLE `webinaire` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `debutDateTime` datetime NOT NULL,
  `finitDateTime` datetime NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  `hotel_id` int(11) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agency`
--
ALTER TABLE `agency`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `userfk_agency` (`user_id`);

--
-- Indexes for table `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_fk` (`userId`);

--
-- Indexes for table `hotels`
--
ALTER TABLE `hotels`
  ADD PRIMARY KEY (`id_hotel`),
  ADD KEY `promotion_fk` (`promotion_id`),
  ADD KEY `agence_fk` (`agency_id`),
  ADD KEY `userFk_hotel` (`user_id`);

--
-- Indexes for table `offer_travel`
--
ALTER TABLE `offer_travel`
  ADD PRIMARY KEY (`id`),
  ADD KEY `agency_id` (`agency_id`),
  ADD KEY `promotion_id` (`promotion_id`);

--
-- Indexes for table `Option`
--
ALTER TABLE `Option`
  ADD PRIMARY KEY (`id`),
  ADD KEY `question_id` (`question_id`);

--
-- Indexes for table `Panier`
--
ALTER TABLE `Panier`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `reservation_id` (`reservation_id`);

--
-- Indexes for table `promotion`
--
ALTER TABLE `promotion`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `title` (`title`);

--
-- Indexes for table `Question`
--
ALTER TABLE `Question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `survey_id` (`survey_id`);

--
-- Indexes for table `reclamation`
--
ALTER TABLE `reclamation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fok_user` (`userId`);

--
-- Indexes for table `Reservation`
--
ALTER TABLE `Reservation`
  ADD PRIMARY KEY (`id_reservation`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `res_transport`
--
ALTER TABLE `res_transport`
  ADD PRIMARY KEY (`id`),
  ADD KEY `res_transport_ibfk_1` (`user_id`),
  ADD KEY `res_transport_ibfk_2` (`car_id`);

--
-- Indexes for table `Survey`
--
ALTER TABLE `Survey`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user` (`created_by`);

--
-- Indexes for table `SurveyResponse`
--
ALTER TABLE `SurveyResponse`
  ADD PRIMARY KEY (`id`),
  ADD KEY `SurveyResponse_ibfk_1` (`user_id`),
  ADD KEY `SurveyResponse_ibfk_2` (`survey_id`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`id_ticket`),
  ADD KEY `FK_agence` (`agency_id`),
  ADD KEY `FK_promotion` (`promotion_id`),
  ADD KEY `userfk_ticket` (`user_id`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `verification_codes`
--
ALTER TABLE `verification_codes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_email` (`email`),
  ADD KEY `idx_email_code` (`email`,`code`);

--
-- Indexes for table `webinaire`
--
ALTER TABLE `webinaire`
  ADD PRIMARY KEY (`id`),
  ADD KEY `hotel_id` (`hotel_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `agency`
--
ALTER TABLE `agency`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `cars`
--
ALTER TABLE `cars`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `hotels`
--
ALTER TABLE `hotels`
  MODIFY `id_hotel` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `offer_travel`
--
ALTER TABLE `offer_travel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `Option`
--
ALTER TABLE `Option`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Panier`
--
ALTER TABLE `Panier`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `promotion`
--
ALTER TABLE `promotion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `Question`
--
ALTER TABLE `Question`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reclamation`
--
ALTER TABLE `reclamation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `Reservation`
--
ALTER TABLE `Reservation`
  MODIFY `id_reservation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `res_transport`
--
ALTER TABLE `res_transport`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `Survey`
--
ALTER TABLE `Survey`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `SurveyResponse`
--
ALTER TABLE `SurveyResponse`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tickets`
--
ALTER TABLE `tickets`
  MODIFY `id_ticket` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `verification_codes`
--
ALTER TABLE `verification_codes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `webinaire`
--
ALTER TABLE `webinaire`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agency`
--
ALTER TABLE `agency`
  ADD CONSTRAINT `userfk_agency` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `user_fk` FOREIGN KEY (`userId`) REFERENCES `User` (`id`);

--
-- Constraints for table `hotels`
--
ALTER TABLE `hotels`
  ADD CONSTRAINT `userFk_hotel` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Option`
--
ALTER TABLE `Option`
  ADD CONSTRAINT `Option_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `Question` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `Panier`
--
ALTER TABLE `Panier`
  ADD CONSTRAINT `Panier_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `Panier_ibfk_2` FOREIGN KEY (`reservation_id`) REFERENCES `Reservation` (`id_reservation`);

--
-- Constraints for table `Question`
--
ALTER TABLE `Question`
  ADD CONSTRAINT `Question_ibfk_1` FOREIGN KEY (`survey_id`) REFERENCES `Survey` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `reclamation`
--
ALTER TABLE `reclamation`
  ADD CONSTRAINT `fok_user` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Reservation`
--
ALTER TABLE `Reservation`
  ADD CONSTRAINT `Reservation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

--
-- Constraints for table `Survey`
--
ALTER TABLE `Survey`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`id`);

--
-- Constraints for table `SurveyResponse`
--
ALTER TABLE `SurveyResponse`
  ADD CONSTRAINT `SurveyResponse_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `SurveyResponse_ibfk_2` FOREIGN KEY (`survey_id`) REFERENCES `Survey` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `userfk_ticket` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `webinaire`
--
ALTER TABLE `webinaire`
  ADD CONSTRAINT `webinaire_fk` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`id_hotel`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 19, 2025 at 05:57 PM
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
-- Table structure for table `Agency`
--

CREATE TABLE `Agency` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Agency`
--

INSERT INTO `Agency` (`id`, `name`, `address`, `phone`, `email`, `image`) VALUES
(1, 'EasyTrip Travel', '123 Main Street', '+216 12345678', 'contact@easytrip.com', NULL),
(2, 'VoyagePlus', '456 Avenue Road', '+216 87654321', 'info@voyageplus.com', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Feedback`
--

CREATE TABLE `Feedback` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `Offer_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Feedback`
--

INSERT INTO `Feedback` (`id`, `user_id`, `message`, `rating`, `Offer_id`, `date`) VALUES
(1, 1, 'Excellent service, very happy with the trip!', 5, 1, '2025-02-17'),
(2, 2, 'Good experience, but the flight was delayed.', 4, 1, '2025-02-17');

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
  `type_room` enum('simple','double','suit') DEFAULT NULL,
  `num_room` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `discount_id` int(11) DEFAULT NULL,
  `agency_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hotels`
--

INSERT INTO `hotels` (`id_hotel`, `name`, `adresse`, `city`, `rating`, `description`, `price`, `type_room`, `num_room`, `image`, `discount_id`, `agency_id`) VALUES
(10, 'yalla', 'zefbuba', 'ziefjij', 5, 'zefazef', 1000, 'simple', 5, 'azefkookazef', NULL, NULL),
(11, 'kaka', 'korba', 'suisse', 5, 'yalla let s go ', 1230, 'simple', 5, 'pojzpefjp', NULL, NULL),
(13, 'zeofkzp', 'zefopzjfop', 'suisssss', 4, 'zefko', 1000, 'simple', 4, 'file:/C:/Users/oussa.LAPTOP-THCQB19P/OneDrive/Images/Captures%20d’écran/Capture%20d\'écran%202024-11-07%20212945.png', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Offer`
--

CREATE TABLE `Offer` (
  `id` int(11) NOT NULL,
  `departure` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `departure_date` date DEFAULT NULL,
  `arrival_date` date DEFAULT NULL,
  `price` float DEFAULT NULL,
  `flightName` varchar(255) DEFAULT NULL,
  `discription` varchar(255) DEFAULT NULL,
  `agency_id` int(11) DEFAULT NULL,
  `promotion_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Offer`
--

INSERT INTO `Offer` (`id`, `departure`, `destination`, `departure_date`, `arrival_date`, `price`, `flightName`, `discription`, `agency_id`, `promotion_id`) VALUES
(1, 'Tunis', 'Paris', '2025-06-15', '2025-06-22', 1200.5, 'Flight Air Tunis', 'A wonderful offer to explore Paris!', NULL, NULL),
(2, 'Sfax', 'London', '2025-07-01', '2025-07-07', 1500.75, 'Flight EasyJet', 'An amazing trip to London!', NULL, NULL);

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
-- Table structure for table `Promotion`
--

CREATE TABLE `Promotion` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `discount_percentage` float DEFAULT NULL,
  `valid_until` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
-- Table structure for table `Reclamation`
--

CREATE TABLE `Reclamation` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `issue` text DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `status` enum('Pending','Resolved') DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Reclamation`
--

INSERT INTO `Reclamation` (`id`, `user_id`, `issue`, `category`, `status`, `date`) VALUES
(1, 1, 'Lost luggage', 'Flight issue', 'Pending', '2025-02-17'),
(2, 1, 'Delayed flight', 'Flight issue', 'Pending', '2025-02-17');

-- --------------------------------------------------------

--
-- Table structure for table `Reservation`
--

CREATE TABLE `Reservation` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `offer_id` int(11) DEFAULT NULL,
  `status` enum('Pending','Completed','Cancelled') DEFAULT NULL,
  `order_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `agence_id` int(11) DEFAULT NULL,
  `promotion_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`id_ticket`, `flight_number`, `airline`, `departure_city`, `arrival_city`, `departure_date`, `departure_time`, `arrival_date`, `arrival_time`, `ticket_class`, `price`, `ticket_type`, `image_airline`, `agence_id`, `promotion_id`) VALUES
(3, 2121, 'zfaezf', 'azefazef', 'fazef', '2025-02-01', '21:00:00', '2025-03-04', '22:00:00', 'First', 1000, 'one-way', NULL, NULL, NULL),
(6, 21, 'suisse ', 'tunisie ', 'equador ', '2025-02-12', '21:00:00', '2025-02-21', '22:00:00', 'Business', 1000, 'round-trip', NULL, NULL, NULL),
(7, 1, 'fix', 'iuhih', 'uguygy', '2025-02-12', '12:00:00', '2025-02-13', '13:00:00', 'Economy', 1000, 'round-trip', NULL, NULL, NULL);

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
(1, 'Admin', 'Admin', 'admin123', 'admin@admin.com', '1234567890', '123 Admin Street', 'admin.jpg', 'Admin'),
(2, 'Agent', 'User', 'agent123', 'agent@example.com', '0987654321', '456 Agent Avenue', 'agent.jpg', 'Agent'),
(4, 'Client', 'User', 'client123', 'client@example.com', '22545445', 'Client Avenue 123', 'client.jpg', 'Client');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Agency`
--
ALTER TABLE `Agency`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `Feedback`
--
ALTER TABLE `Feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `Offer_id` (`Offer_id`);

--
-- Indexes for table `hotels`
--
ALTER TABLE `hotels`
  ADD PRIMARY KEY (`id_hotel`),
  ADD UNIQUE KEY `discount_id` (`discount_id`),
  ADD KEY `fk_hotels_agency` (`agency_id`);

--
-- Indexes for table `Offer`
--
ALTER TABLE `Offer`
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
-- Indexes for table `Promotion`
--
ALTER TABLE `Promotion`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Question`
--
ALTER TABLE `Question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `survey_id` (`survey_id`);

--
-- Indexes for table `Reclamation`
--
ALTER TABLE `Reclamation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `Reservation`
--
ALTER TABLE `Reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `offer_id` (`offer_id`);

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
  ADD KEY `fk_agence` (`agence_id`),
  ADD KEY `fk_promotion` (`promotion_id`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Agency`
--
ALTER TABLE `Agency`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `Feedback`
--
ALTER TABLE `Feedback`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `hotels`
--
ALTER TABLE `hotels`
  MODIFY `id_hotel` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `Offer`
--
ALTER TABLE `Offer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
-- AUTO_INCREMENT for table `Promotion`
--
ALTER TABLE `Promotion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Question`
--
ALTER TABLE `Question`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Reclamation`
--
ALTER TABLE `Reclamation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `Reservation`
--
ALTER TABLE `Reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

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
  MODIFY `id_ticket` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Feedback`
--
ALTER TABLE `Feedback`
  ADD CONSTRAINT `Feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `Feedback_ibfk_2` FOREIGN KEY (`Offer_id`) REFERENCES `Offer` (`id`);

--
-- Constraints for table `Offer`
--
ALTER TABLE `Offer`
  ADD CONSTRAINT `Offer_ibfk_1` FOREIGN KEY (`agency_id`) REFERENCES `Agency` (`id`),
  ADD CONSTRAINT `Offer_ibfk_2` FOREIGN KEY (`promotion_id`) REFERENCES `Promotion` (`id`);

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
  ADD CONSTRAINT `Panier_ibfk_2` FOREIGN KEY (`reservation_id`) REFERENCES `Reservation` (`id`);

--
-- Constraints for table `Question`
--
ALTER TABLE `Question`
  ADD CONSTRAINT `Question_ibfk_1` FOREIGN KEY (`survey_id`) REFERENCES `Survey` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `Reclamation`
--
ALTER TABLE `Reclamation`
  ADD CONSTRAINT `Reclamation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

--
-- Constraints for table `Reservation`
--
ALTER TABLE `Reservation`
  ADD CONSTRAINT `Reservation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `Reservation_ibfk_2` FOREIGN KEY (`offer_id`) REFERENCES `Offer` (`id`);

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

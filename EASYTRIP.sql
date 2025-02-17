-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 17, 2025 at 08:39 PM
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
  `logo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Agency`
--

INSERT INTO `Agency` (`id`, `name`, `address`, `phone`, `email`, `logo`) VALUES
(1, 'EasyTrip Travel', '123 Main Street', '+216 12345678', 'contact@easytrip.com', NULL),
(2, 'VoyagePlus', '456 Avenue Road', '+216 87654321', 'info@voyageplus.com', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `BilletAvion`
--

CREATE TABLE `BilletAvion` (
  `id_billet` int(11) NOT NULL,
  `num_vol` varchar(50) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `compagnie_aerienne` varchar(100) NOT NULL,
  `ville_depart` varchar(100) NOT NULL,
  `ville_arrivee` varchar(100) NOT NULL,
  `date_depart` date NOT NULL,
  `heure_depart` time NOT NULL,
  `date_arrivee` date NOT NULL,
  `heure_arrivee` time NOT NULL,
  `classe` enum('Economique','Affaires','Première') NOT NULL,
  `prix` decimal(10,2) NOT NULL CHECK (`prix` >= 0),
  `type_billet` enum('Aller simple','Aller-retour') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Table structure for table `OffreHotel`
--

CREATE TABLE `OffreHotel` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `adresse` varchar(255) NOT NULL,
  `ville` varchar(100) NOT NULL,
  `categorie` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `prix_par_nuit` decimal(10,2) NOT NULL,
  `type_chambre` varchar(50) NOT NULL,
  `nombre_chambres_disponibles` int(11) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `category` enum('Crash','UI Issue','Performance','Feature Not Working','Other') DEFAULT NULL,
  `status` enum('Pending','Resolved') DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(1, 'Admin', 'User', 'admin123', 'admin@example.com', '1234567890', '123 Admin Street', 'admin.jpg', 'Admin'),
(2, 'Agent', 'User', 'agent123', 'agent@example.com', '0987654321', '456 Agent Avenue', 'agent.jpg', 'Agent');

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
-- Indexes for table `BilletAvion`
--
ALTER TABLE `BilletAvion`
  ADD PRIMARY KEY (`id_billet`),
  ADD UNIQUE KEY `num_vol` (`num_vol`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `Feedback`
--
ALTER TABLE `Feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `Offer_id` (`Offer_id`);

--
-- Indexes for table `Offer`
--
ALTER TABLE `Offer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `agency_id` (`agency_id`),
  ADD KEY `promotion_id` (`promotion_id`);

--
-- Indexes for table `OffreHotel`
--
ALTER TABLE `OffreHotel`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`);

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
-- AUTO_INCREMENT for table `BilletAvion`
--
ALTER TABLE `BilletAvion`
  MODIFY `id_billet` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Feedback`
--
ALTER TABLE `Feedback`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Offer`
--
ALTER TABLE `Offer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `OffreHotel`
--
ALTER TABLE `OffreHotel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

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
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `BilletAvion`
--
ALTER TABLE `BilletAvion`
  ADD CONSTRAINT `BilletAvion_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `User` (`id`) ON DELETE SET NULL;

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
-- Constraints for table `OffreHotel`
--
ALTER TABLE `OffreHotel`
  ADD CONSTRAINT `OffreHotel_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `User` (`id`) ON DELETE SET NULL;

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

-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 25, 2014 at 09:04 PM
-- Server version: 5.5.35
-- PHP Version: 5.3.10-1ubuntu3.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `planetsim`
--

-- --------------------------------------------------------

--
-- Table structure for table `experiment`
--

CREATE TABLE IF NOT EXISTS `experiment` (
  `experiment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `experiment_name` varchar(100) NOT NULL,
  `grid_spacing` int(11) NOT NULL,
  `time_step` int(11) NOT NULL,
  `simulation_length` int(11) NOT NULL,
  `data_precision` int(11) NOT NULL,
  `geo_precision` int(11) NOT NULL,
  `temporal_precision` int(11) NOT NULL,
  `axial_tilt` double NOT NULL,
  `orbital_eccentricity` double NOT NULL,
  PRIMARY KEY (`experiment_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=82 ;

-- --------------------------------------------------------

--
-- Table structure for table `grid_points`
--

CREATE TABLE IF NOT EXISTS `grid_points` (
  `date_time` datetime NOT NULL,
  `top_latitude` int(11) NOT NULL,
  `left_longitude` int(11) NOT NULL,
  `temperature` double NOT NULL,
  `grid_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `experiment_id` bigint(20) NOT NULL,
  PRIMARY KEY (`grid_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15702 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

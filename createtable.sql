# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.16)
# Database: newsspider
# Generation Time: 2017-11-07 07:34:32 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table news_baidu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `news_baidu`;

CREATE TABLE `news_baidu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text,
  `keyword` text,
  `title` text,
  `writer` text,
  `publish_time` text,
  `source` text,
  `body` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table news_sina
# ------------------------------------------------------------

DROP TABLE IF EXISTS `news_sina`;

CREATE TABLE `news_sina` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text,
  `keyword` text,
  `title` text,
  `writer` text,
  `publish_time` text,
  `source` text,
  `body` text,
  `yangzhou_guazhou` int(11) DEFAULT '0',
  `zhenjiang_changjiang` int(11) DEFAULT '0',
  `beijing_guoji` int(11) DEFAULT '0',
  `shanghai_jiandan` int(11) DEFAULT '0',
  `hangzhou_xihu` int(11) DEFAULT '0',
  `chengdu_rebo` int(11) DEFAULT '0',
  `changsha_juzhou` int(11) DEFAULT '0',
  `caomei` int(11) DEFAULT '0',
  `zhangbei_caoyuan` int(11) DEFAULT '0',
  `zhoushan_donghai` int(11) DEFAULT '0',
  `midi` int(11) DEFAULT '0',
  `nanjing_senlin` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table news_sogou
# ------------------------------------------------------------

DROP TABLE IF EXISTS `news_sogou`;

CREATE TABLE `news_sogou` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text,
  `keyword` text,
  `title` text,
  `writer` text,
  `publish_time` text,
  `source` text,
  `body` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table news_toutiao
# ------------------------------------------------------------

DROP TABLE IF EXISTS `news_toutiao`;

CREATE TABLE `news_toutiao` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` text,
  `keyword` text,
  `title` text,
  `writer` text,
  `publish_time` text,
  `source` text,
  `body` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

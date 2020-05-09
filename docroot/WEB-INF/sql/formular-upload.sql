
use cjarges_portlets;

DROP TABLE IF EXISTS `cjarges_portlets`.`formular_upload_portlet`;

CREATE TABLE `cjarges_portlets`.`formular_upload_portlet` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nume` varchar(45) NOT NULL,
  `prenume` varchar(45) NOT NULL,
  `telefon` varchar(45) NOT NULL,
  `email` varchar(60) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabela colectare detalii formular upload';

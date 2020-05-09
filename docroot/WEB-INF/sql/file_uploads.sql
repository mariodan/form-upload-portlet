
use cjarges_portlets;

DROP TABLE IF EXISTS `cjarges_portlets`.`file_uploads`;

CREATE TABLE `file_uploads` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nume_fisier` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `path` varchar(245) COLLATE utf8_unicode_ci DEFAULT NULL,
  `size_in_kb` int(11) DEFAULT NULL,
  `id_form_upload` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_form_upload_idx` (`id_form_upload`),
  CONSTRAINT `fk_form_upload` FOREIGN KEY (`id_form_upload`) REFERENCES `formular_upload_portlet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Tabela fisiere upload';


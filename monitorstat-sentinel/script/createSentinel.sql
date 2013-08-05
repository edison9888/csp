/*Table structure for table `sentinel_black_app` */

DROP TABLE IF EXISTS `sentinel_black_app`;

CREATE TABLE `sentinel_black_app` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_black_app` */

/*Table structure for table `sentinel_black_customer` */

DROP TABLE IF EXISTS `sentinel_black_customer`;

CREATE TABLE `sentinel_black_customer` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `customer_info` varchar(256) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_black_customer` */

/*Table structure for table `sentinel_black_interface` */

DROP TABLE IF EXISTS `sentinel_black_interface`;

CREATE TABLE `sentinel_black_interface` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `interface_info` varchar(256) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_black_interface` */

/*Table structure for table `sentinel_config_version` */

DROP TABLE IF EXISTS `sentinel_config_version`;

CREATE TABLE `sentinel_config_version` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_config_version` */

/*Table structure for table `sentinel_data_url` */

DROP TABLE IF EXISTS `sentinel_data_url`;

CREATE TABLE `sentinel_data_url` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `data_url` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_data_url` */

/*Table structure for table `sentinel_flow_app` */

DROP TABLE IF EXISTS `sentinel_flow_app`;

CREATE TABLE `sentinel_flow_app` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `limit_flow` int(11) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_flow_app` */

/*Table structure for table `sentinel_flow_dependency` */

DROP TABLE IF EXISTS `sentinel_flow_dependency`;

CREATE TABLE `sentinel_flow_dependency` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `interface_info` varchar(256) DEFAULT NULL,
  `limit_flow` int(11) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_flow_dependency` */

/*Table structure for table `sentinel_flow_interface` */

DROP TABLE IF EXISTS `sentinel_flow_interface`;

CREATE TABLE `sentinel_flow_interface` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `interface_info` varchar(256) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `limit_flow` int(11) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_flow_interface` */

/*Table structure for table `sentinel_interface_invoke` */

DROP TABLE IF EXISTS `sentinel_interface_invoke`;

CREATE TABLE `sentinel_interface_invoke` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `interface_info` varchar(256) DEFAULT NULL,
  `estimate_qps` int(11) DEFAULT NULL,
  `strong` varchar(44) DEFAULT NULL,
  `remark` varchar(256) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_interface_invoke` */

/*Table structure for table `sentinel_ref_ips` */

DROP TABLE IF EXISTS `sentinel_ref_ips`;

CREATE TABLE `sentinel_ref_ips` (
  `id` varchar(44) NOT NULL,
  `ip_addr` varchar(44) DEFAULT NULL,
  `ref_id` varchar(44) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_id` (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_ref_ips` */

/*Table structure for table `sentinel_user_permission` */

DROP TABLE IF EXISTS `sentinel_user_permission`;

CREATE TABLE `sentinel_user_permission` (
  `id` varchar(44) NOT NULL,
  `mail` varchar(44) DEFAULT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `wangwang` varchar(44) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_user_permission` */

insert  into `sentinel_user_permission`(`id`,`mail`,`app_name`,`wangwang`,`level`) values ('dukun','dukun@taobao.com','all','杜琨',0),('xiaoxie','xiaoxie@taobao.com','all','小邪',0),('youji','youji.zj@taobao.com','all','游骥',0),('zhuliu','zhuliu@taobao.com','all','逐流',0);

/*Table structure for table `sentinel_white_customer` */

DROP TABLE IF EXISTS `sentinel_white_customer`;

CREATE TABLE `sentinel_white_customer` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `customer_info` varchar(256) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sentinel_white_customer` */

/*Table structure for table `sentinel_white_interface` */

DROP TABLE IF EXISTS `sentinel_white_interface`;

CREATE TABLE `sentinel_white_interface` (
  `id` varchar(44) NOT NULL,
  `app_name` varchar(44) DEFAULT NULL,
  `interface_info` varchar(256) DEFAULT NULL,
  `ref_app` varchar(44) DEFAULT NULL,
  `user` varchar(44) DEFAULT NULL,
  `version` varchar(44) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


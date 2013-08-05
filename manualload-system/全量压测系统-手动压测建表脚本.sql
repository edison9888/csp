-- 压测应用配置表
CREATE TABLE `app_config` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
   `app_name` varchar(100) DEFAULT NULL COMMENT '应用名称',
   `pre_kinds` varchar(40) DEFAULT NULL COMMENT '压测类别: all 表示全部 half表示一半',
   `pre_type` varchar(40) DEFAULT 'httpload' COMMENT '压测类型',
   `pre_way` int(11) DEFAULT '1' COMMENT '压测方式：1 表示手动 0表示自动',
   `user_name` varchar(40) DEFAULT NULL COMMENT '压测人名称',
   `user_pass` varchar(40) DEFAULT NULL COMMENT '压测人密码',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8


-- 压测应用机器表
CREATE TABLE `app_machine` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
   `app_name` varchar(100) DEFAULT NULL COMMENT '应用名',
   `app_id` varchar(100) DEFAULT NULL COMMENT '应用ID',
   `mac_name` varchar(100) DEFAULT NULL COMMENT '应用机器名',
   `mac_os` varchar(40) DEFAULT NULL COMMENT '执行机器系统',
   `mac_ip` varchar(40) DEFAULT NULL COMMENT '执行机器ip',
   `mac_state` varchar(40) DEFAULT NULL COMMENT '执行机器状态',
   `mac_totle` int(11) DEFAULT '0' COMMENT '机器总数',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

-- 压测结果信息表
CREATE TABLE `pressure_result` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
   `request_totle` int(11) DEFAULT '0' COMMENT '总请求数',
   `process_count` int(11) DEFAULT '0' COMMENT '最大并发进程数',
   `bytes_totle` int(11) DEFAULT '0' COMMENT '总传输的数据量。单位:字节',
   `time_totle` int(11) DEFAULT '0' COMMENT '总耗时。单位:秒',
   `bytes_connection` int(11) DEFAULT '0' COMMENT '连接平均传输的数据量。单位:字节',
   `fetches_sec` int(11) DEFAULT '0' COMMENT '每秒的响应请求数',
   `bytes_sec` int(11) DEFAULT '0' COMMENT '每秒传递数据量。单位:字节',
   `connect_avg` int(11) DEFAULT '0' COMMENT '连接平均响应时间。单位:毫秒',
   `connect_max` int(11) DEFAULT '0' COMMENT '连接最大响应时间。单位:毫秒',
   `connect_min` int(11) DEFAULT '0' COMMENT '连接最小响应时间。单位:毫秒',
   `response_avg` int(11) DEFAULT '0' COMMENT '请求平均响应时间。单位:毫秒',
   `response_max` int(11) DEFAULT '0' COMMENT '请求最大响应时间。单位:毫秒',
   `response_min` int(11) DEFAULT '0' COMMENT '请求最小响应时间。单位:毫秒',
   `bad_count` int(11) DEFAULT '0' COMMENT '同一个http请求，不一样的结果的个数',
   `app_id` int(11) DEFAULT NULL COMMENT '应用id',
   `user_name` varchar(40) DEFAULT NULL COMMENT '用户名',
   `mac_name` varchar(40) DEFAULT NULL COMMENT '压测机器名称',
   `httpState_stateCount` varchar(400) DEFAULT NULL COMMENT 'http状态和该状态下面的请求数。',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
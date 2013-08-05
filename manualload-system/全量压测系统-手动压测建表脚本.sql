-- ѹ��Ӧ�����ñ�
CREATE TABLE `app_config` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����id',
   `app_name` varchar(100) DEFAULT NULL COMMENT 'Ӧ������',
   `pre_kinds` varchar(40) DEFAULT NULL COMMENT 'ѹ�����: all ��ʾȫ�� half��ʾһ��',
   `pre_type` varchar(40) DEFAULT 'httpload' COMMENT 'ѹ������',
   `pre_way` int(11) DEFAULT '1' COMMENT 'ѹ�ⷽʽ��1 ��ʾ�ֶ� 0��ʾ�Զ�',
   `user_name` varchar(40) DEFAULT NULL COMMENT 'ѹ��������',
   `user_pass` varchar(40) DEFAULT NULL COMMENT 'ѹ��������',
   `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8


-- ѹ��Ӧ�û�����
CREATE TABLE `app_machine` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����id',
   `app_name` varchar(100) DEFAULT NULL COMMENT 'Ӧ����',
   `app_id` varchar(100) DEFAULT NULL COMMENT 'Ӧ��ID',
   `mac_name` varchar(100) DEFAULT NULL COMMENT 'Ӧ�û�����',
   `mac_os` varchar(40) DEFAULT NULL COMMENT 'ִ�л���ϵͳ',
   `mac_ip` varchar(40) DEFAULT NULL COMMENT 'ִ�л���ip',
   `mac_state` varchar(40) DEFAULT NULL COMMENT 'ִ�л���״̬',
   `mac_totle` int(11) DEFAULT '0' COMMENT '��������',
   `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

-- ѹ������Ϣ��
CREATE TABLE `pressure_result` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '����id',
   `request_totle` int(11) DEFAULT '0' COMMENT '��������',
   `process_count` int(11) DEFAULT '0' COMMENT '��󲢷�������',
   `bytes_totle` int(11) DEFAULT '0' COMMENT '�ܴ��������������λ:�ֽ�',
   `time_totle` int(11) DEFAULT '0' COMMENT '�ܺ�ʱ����λ:��',
   `bytes_connection` int(11) DEFAULT '0' COMMENT '����ƽ�����������������λ:�ֽ�',
   `fetches_sec` int(11) DEFAULT '0' COMMENT 'ÿ�����Ӧ������',
   `bytes_sec` int(11) DEFAULT '0' COMMENT 'ÿ�봫������������λ:�ֽ�',
   `connect_avg` int(11) DEFAULT '0' COMMENT '����ƽ����Ӧʱ�䡣��λ:����',
   `connect_max` int(11) DEFAULT '0' COMMENT '���������Ӧʱ�䡣��λ:����',
   `connect_min` int(11) DEFAULT '0' COMMENT '������С��Ӧʱ�䡣��λ:����',
   `response_avg` int(11) DEFAULT '0' COMMENT '����ƽ����Ӧʱ�䡣��λ:����',
   `response_max` int(11) DEFAULT '0' COMMENT '���������Ӧʱ�䡣��λ:����',
   `response_min` int(11) DEFAULT '0' COMMENT '������С��Ӧʱ�䡣��λ:����',
   `bad_count` int(11) DEFAULT '0' COMMENT 'ͬһ��http���󣬲�һ���Ľ���ĸ���',
   `app_id` int(11) DEFAULT NULL COMMENT 'Ӧ��id',
   `user_name` varchar(40) DEFAULT NULL COMMENT '�û���',
   `mac_name` varchar(40) DEFAULT NULL COMMENT 'ѹ���������',
   `httpState_stateCount` varchar(400) DEFAULT NULL COMMENT 'http״̬�͸�״̬�������������',
   `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
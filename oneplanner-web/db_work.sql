CREATE USER 'ontide'@'%' IDENTIFIED BY 'Ontide01^';
CREATE USER 'ontide'@'localhost' IDENTIFIED BY 'Ontide01^';
CREATE USER 'ontide'@'127.0.0.1' IDENTIFIED BY 'Ontide01^';
CREATE USER 'ontide'@'::1' IDENTIFIED BY 'Ontide01^';
GRANT ALL PRIVILEGES ON *.* TO 'ontide'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'ontide'@'localhost';
FLUSH PRIVILEGES;

CREATE DATABASE oneplandb;

use oneplandb;

/* test용 database*/
CREATE USER 'devontide'@'%' IDENTIFIED BY 'Ontide01^';
CREATE USER 'devontide'@'localhost' IDENTIFIED BY 'Ontide01^';
CREATE USER 'devontide'@'127.0.0.1' IDENTIFIED BY 'Ontide01^';
CREATE USER 'devontide'@'::1' IDENTIFIED BY 'Ontide01^';
GRANT ALL PRIVILEGES ON *.* TO 'devontide'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'devontide'@'localhost';
FLUSH PRIVILEGES;

CREATE DATABASE devoneplandb;

use devoneplandb;


/* drop tables */
drop table user_info;
drop table task_info;
drop table schedule_info;
drop table schedule_history;
drop table admin_info;


CREATE TABLE user_info (
 user_id varchar(200) NOT NULL, /* id, email address*/
 user_name varchar(200) NOT NULL, 
 user_type char(1) NOT NULL, /* 'G':google+, 'F':facebook, 'N':Normal */
 passwd varchar(200) NOT NULL,
 email varchar(200) NOT NULL,
 sex char(1) NULL, /* 'M', 'F' */
 birth_date char(8) NULL, /* yyyymmdd */
 cal_type char(1) NOT NULL, /* calendar type 'G':Google, 'F':Facebook, 'N':Naver, 'D':Daum */
 cal_view_type char(1) NOT NULL, /* calendar view type('B':biweek, 'M':monthly, 'J':Jobs) */
 start_week char(1) NOT NULL, /* 'S':Sunday, 'M':Monday */
 time_zone varchar(50) NOT NULL, /* Asia/Seoul ref: https://gist.github.com/arpit/1035596 */
 lang_code char(2) default 'ko', /* 한국어(기본값):ko, 영어:en */
 widget_option char(8) default '00000000', /* bitoption : Hexa value */
 auth_yn char(1) default 'N', /* 등록 이메일 인증  Y : default N*/
 delete_yn char(1) default 'N', /* 삭제여부 Y/N */
 auth_mode char(1) default 'N', /* 가입인증메일전송:A,가입증확인:Y,  비번리셋확인메일전송:R, 비번리셋완료:S */
 confirm_date char(14) NULL,
 create_date  datetime default  now(),
 update_date  datetime default  now(),
 PRIMARY KEY (user_id),
 KEY idx_user_info_01 (user_id, user_type),
 KEY idx_user_info_02 (user_id, sex)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE auth_info (    
 user_id varchar(200) NOT NULL, /* id, email address*/
 auth_id varchar(1024) NOT NULL,
 confirm_yn char(1) default 'N', 
 auth_mode char(1), /* 가입인증메일전송:A,비번리셋메일전송:R */
 expired_date char(14) NOT NULL,
 update_date  datetime default  NOW(),
 PRIMARY KEY (user_id, auth_id),
 KEY idx_auth_info_01 (user_id, auth_mode)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE mailing_history (
 user_id varchar(200) NOT NULL, /* id, email address*/
 send_date char(14) NOT NULL,
 auth_mode char(1) default 'A', /* 가입인증메일전송:A,가입증확인:Y, 비번리셋메일전송:R, 비번리셋완료:S  */
 auth_id varchar(1024) NULL,
 /* confirm_yn char(1) default 'N', */
 create_date  datetime default  NOW(),
 PRIMARY KEY (user_id, send_date),
 KEY idx_mailing_history_01 (user_id, auth_mode)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE task_info (
 user_id varchar(200) NOT NULL, /* id, email address*/
 task_id bigint(20) NOT NULL,  /*app에서 지정한 integer값(시퀀스)*/
 task_name varchar(200) NOT NULL, 
 task_type char(1) DEFAULT  'G', /* 'G':Goal, 'R':Routine */
 start_date char(12) NULL, /* 시작일자 */
 end_date char(12) NULL, /* 종료일자 */
 frequency_cnt int NOT NULL, /* 빈도 */
 frequency_period char(1) NOT NULL, /* 빈도기간 월,주,일(M, W, D) */
 progress int NOT NULL, /* 0-100 진도(%) */
 participants varchar(1024) NULL, /* emailaddress/userId등의 유니크한 값 ';'구분자로 처리 
    ex) aaa@gmail.com;01044445555;bbb0123… */ 
 color int NOT NULL, /* Task 의 색상 */
 days char(8) NULL, /* 총 8자 : 월,화,수,목,금,토,일,매일 , ex) "010101010" */
 weeks char(5) NULL, /* 총 5글자 : 1주,2주,3주,4주,5주 , ex) "10000" */
 start_time char(4) NULL, /* 시작시간*/
 end_time char(4) NULL, /* 종료시간 */
 delete_yn char(1) default 'N', /* 삭제여부 Y/N */
 create_date  datetime default  now(),
 update_date  datetime default  now(),
 PRIMARY KEY (user_id,task_id),
  KEY idx_task_info_01 (user_id, task_id, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;
    
    
CREATE TABLE schedule_info (
 user_id varchar(200) NOT NULL,
 schedule_id bigint(20) NOT NULL, 
 schedule_name varchar(200) NOT NULL,
 start_date char(12) NOT NULL,
 end_date char(12) NOT NULL,
 memo Text NULL,
 task_id bigint(20) NOT NULL,
 participants varchar(1024) NULL, 
 location varchar(200) NULL,
 location_url varchar(1024) NULL,
 alarm_yn char(1) default 'N', /* 알람처리 여부 Y/N */ 
 complete_yn char(1) default 'N', /* 완료여부 Y/N */
 days char(8) NULL, /* 총 8자 : 월,화,수,목,금,토,일,매일 , ex) "010101010" */
 weeks char(5) NULL, /* 총 5글자 : 1주,2주,3주,4주,5주 , ex) "10000" */
 start_time char(4) NULL, /* 시작시간*/
 end_time char(4) NULL, /* 종료시간 */
 delete_yn char(1) default 'N', /* 삭제여부 Y/N */
 group_id bigint(20) NOT NULL, /* schedule group id */
 alarm_period int NOT NULL, /*  -1 / 1 / 2 / 3 / 4 /5 : 0분, 5분, 10분, 15분, 30분, 60분 */
 repeat_end_date  char(12) NOT NULL, 
 create_date  datetime default  now(),
 update_date  datetime default  now(),
 PRIMARY KEY (user_id, schedule_id),
 KEY idx_schedule_info_01 (user_id, schedule_id, task_id),
 KEY idx_schedule_info_02 (user_id, schedule_id, start_date)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE schedule_history (
 user_id varchar(200) NOT NULL,
 schedule_id bigint(20) NOT NULL, 
 now_date char(12) NOT NULL,
 start_date char(12) NOT NULL,
 end_date char(12) NOT NULL,
 complete_yn char(1) default 'N', /* 완료여부 Y/N */
 delete_yn char(1) default 'N',  /* 삭제 여부 */
 create_date  datetime default  now(),
 update_date  datetime default  now(),
 PRIMARY KEY (user_id, schedule_id, now_date)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE today_info (    
 today varchar(8) NOT NULL, /* yyyymmdd*/
 cont_seq int NOT NULL,
 title TEXT NULL,
 content TEXT NULL,
 image_type char(1) NULL, /* I: 내부이미지파일, O:외부 이미지 파일 */
 image_url varchar(1024), 
 create_date  datetime default  now(),
 update_date  datetime default  now(),
 PRIMARY KEY (today, cont_seq)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

CREATE TABLE admin_info (    
 user_id varchar(100) NOT NULL,
 passwd  varchar(100) NOT NULL,
 email  varchar(100) NOT NULL,
 access_date  datetime default  now(),
 update_date  datetime default  now(),
 PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=euckr;

/*  테스트 데이터 */
INSERT INTO `oneplandb`.`user_info`
(`user_id`,
`user_name`,
`user_type`,
passwd,
`email`,
`sex`,
`birth_date`,
`cal_type`,
`cal_view_type`,
`start_week`,
`time_zone`,
`widget_option`)
VALUES 
('jinnonsbox@gmail.com',
'jinwon choi',
'F',
'passwd',
'jinnonsbox@gmail.com',
'M',
'710624',
'G',
'B',
'M',
'Asia/Seoul',
'00000001');

INSERT INTO `oneplandb`.`user_info`
(`user_id`,
`user_name`,
`user_type`,
passwd,
`email`,
`sex`,
`birth_date`,
`cal_type`,
`cal_view_type`,
`start_week`,
`time_zone`,
`widget_option`)
VALUES 
('jinnon',
'최진원',
'N',
'passwd',
'jinnon@naver.com',
'M',
'720624',
'F',
'M',
'S',
'Europe/Amsterdam',
'00000010');


INSERT INTO `oneplandb`.`user_info`
(`user_id`,
`user_name`,
`user_type`,
passwd,
`email`,
`sex`,
`birth_date`,
`cal_type`,
`cal_view_type`,
`start_week`,
`time_zone`,
`widget_option`)
VALUES 
('coolturk',
'최진원2',
'G',
'passwd',
'jinnonspot@google.com',
'M',
'730624',
'G',
'J',
'S',
'Pacific/Truk',
'00000100');

INSERT INTO `oneplandb`.`task_info`
(`user_id`,
`task_id`,
`task_name`,
`task_type`,
`due_date`,
`frequency_cnt`,
`frequency_period`,
`progress`,
`participants`)
VALUES
('jinnonsbox@gmail.com',
3,
'OnePlanner 구축하기',
'G',
'20170201',
1,
'W',
0,
'배강용사장;배익성부장;삼성분들');

INSERT INTO `oneplandb`.`task_info`
(`user_id`,
`task_id`,
`task_name`,
`task_type`,
`due_date`,
`frequency_cnt`,
`frequency_period`,
`progress`,
`participants`)
VALUES
('jinnonsbox@gmail.com',
4,
'OnePlanner 운영하기',
'R',
'20171231',
1,
'M',
0,
'삼성분들;운영팀');
INSERT INTO `oneplandb`.`task_info`
(`user_id`,
`task_id`,
`task_name`,
`task_type`,
`due_date`,
`frequency_cnt`,
`frequency_period`,
`progress`,
`participants`)
VALUES
('jinnonsbox@gmail.com',
5,
'OnePlanner 확장하기',
'G',
'20180101',
1,
'D',
0,
'유드림;개발팀');

INSERT INTO `oneplandb`.`schedule_info`
(`user_id`,
`schedule_id`,
`schedule_name`,
`start_date`,
`end_date`,
`memo`,
`task_id`,
`participants`,
`location`,
`location_url`,
`alarm_yn`,
`complete_yn`,
`delete_yn`)
VALUES
('jinnonsbox@gmail.com',
1,
'DB생성하기',
'20170107',
'20170109',
'테이블레이아웃 필수  작성완료',
3,
'유드림;배익성부장',
'삼성동',
'https://www.google.co.kr/maps/place/18+Bongeunsa-ro+61-gil,+Gangnam-gu,+Seoul/@37.5135116,127.0464762,18z/data=!4m5!3m4!1s0x357ca40c911a569b:0x61e4b36380e01bfe!8m2!3d37.5123393!4d127.0462724',
'Y',
'N',
'N');

INSERT INTO `oneplandb`.`schedule_info`
(`user_id`,
`schedule_id`,
`schedule_name`,
`start_date`,
`end_date`,
`memo`,
`task_id`,
`participants`,
`location`,
`location_url`,
`alarm_yn`,
`complete_yn`,
`delete_yn`)
VALUES
('jinnonsbox@gmail.com',
2,
'웹서버개발하기',
'20170109',
'20170119',
'스프링 작성완료',
3,
'유드림;배익성부장',
'판교 CnC',
'https://www.google.co.kr/maps/place/SK%EC%A3%BC%EC%8B%9D%ED%9A%8C%EC%82%ACC%26C+%ED%8C%90%EA%B5%90%EC%BA%A0%ED%8D%BC%EC%8A%A4A/@37.405725,127.0983423,15z/data=!4m5!3m4!1s0x0:0x4db6defb0b46f3f!8m2!3d37.405725!4d127.0983423',
'Y',
'N',
'N');

INSERT INTO `oneplandb`.`schedule_info`
(`user_id`,
`schedule_id`,
`schedule_name`,
`start_date`,
`end_date`,
`memo`,
`task_id`,
`participants`,
`location`,
`location_url`,
`alarm_yn`,
`complete_yn`,
`delete_yn`)
VALUES
('jinnonsbox@gmail.com',
3,
'웹서버성능테스트',
'20170119',
'20170129',
'JMeter구축하기',
3,
'유드림;배익성부장',
'현대백화점',
'https://www.google.co.kr/maps/dir/''/%EC%82%BC%EC%84%B1%EC%97%AD+%ED%98%84%EB%8C%80%EB%B0%B1%ED%99%94%EC%A0%90/@37.5085227,126.9897288,12z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x357ca4153126826f:0x926f3f9988d8ba62!2m2!1d127.0597688!2d37.5085434',
'Y',
'N',
'N');




Oneplanet README
================
0. 사전 확인 사항
메일을 통해 전달한 AWS서버 접속방법은 메일을 통해 전달한 "ontide 서버접속.zip"파일을 확인한다.
AWS는 console.aws.amazon.com로 접속하고 ontide에서 등록한 계정으로 들어가면 확인가능
이전에 전달한 AWS설정결과 메일로 내역을 확인할것.

OnePlanner서버웹 개발환경 구축에 대해 설명한다.

1. jdk 설치
jdk1.7 필수.

2. eclipse 설정
전자정부프레임웍(www.egovframe.go.kr)에서 제공하는 eclipse버전을 사용했는데 
아래 url경로에서 다운받아 압축을 푼다.
https://www.dropbox.com/s/dq2uc2i0lt784st/eGovFrameDev-3.5.1-64bit.zip?dl=0
해당 eclipse 하단 workspace경로에(eGovFrameDev-3.5.1-64bit\workspace\oneplanner-web) 소스 프로젝트를 확인.

1. MySql db 설정
Mysql 5.7로 구축되어 있고, 
다음 파일을 아래 url을 통해서 다운받아 설치한다.

mysql-installer-community-5.7.14.0.msi
https://www.dropbox.com/s/zow85g4giph9dl6/mysql-installer-community-5.7.14.0.msi?dl=0
==>mysql 서버 

mysql-workbench-community-6.3.7-win32.msi
https://www.dropbox.com/s/zow85g4giph9dl6/mysql-installer-community-5.7.14.0.msi?dl=0
==>mysql client툴

1.1. MySQL Server 5.7 설정
Mysql을 설치하면 아래와 같은 경로에 설정파일이 있는데 해당 항목으로 section에 맞게 추가한다.

 C:\ProgramData\MySQL\MySQL Server 5.7\my.ini
[mysql]
default-character-set=utf8
[mysqld]
transaction-isolation = READ-COMMITTED
port=3306
lower_case_table_names = 1
innodb_lock_wait_timeout = 20
innodb_thread_concurrency = 10

1.2. DB설치
1.2.1. root계정 passwd설정
cmd창을 실행
cd C:\Program Files\MySQL\MySQL Server 5.7\bin
mysql -u root
mysql 콘솔에서 다음을 실행하여 passwd설정
mysql> alter user 'root'@'localhost' IDENTIFIED BY '123456';

1.2.2. ontide db설치
eclipse workspace에서 oneplanner-web 프로젝트의 db_work.sql을 열고,
128라인까지 각 ddl 스크립트를 실행한다.

1.3. mysql client 설정
먼저 설치한 mysql-workbench를 띄우고, dabase > Manage Connections..를 선택
다음 항목을 입력하고 test connection을 클릭하여 접속 여부를 확인한다.
==>local db접속 
Connection Name:Local instance MySQL57
Hostname:127.0.0.1
username:ontide 
password:....
defaultschema:oneplandb

==>server db접속 
Connection Name:ontidereal
Hostname:13.124.1.173
username:ontide 
password:....
defaultschema:oneplandb
-
접속한 후 생성된 테이블을 확인

2. eclipse 실행

2.1. 개발환경 확인
- 
- maven3.2.2(환경배포방식) 사용
  user setting: D:\dev\mvnrepository_3.5\settings.xml
- java 1.7 
- spring framework 4.0.3
  
2.1.1. maven설정
  windows > preference > Maven > User Setting
   - User Settings  (settings.xml파일은 Local Repository 지정 확인)
   - Local Repository
   를 지정할것.
  >> pom.xml(maven file)에 web에서 필요로 하는 모든 library가 지정되어있음
  
2.1.2. config 파일 설정
웹 환경설정파일은 profile/prod 이하 경로에 위치한다.
(이 설정파일들은 실제 사용될 src/main/resources로 자동배포됨)
- datasource.properties DB관련설정으로 local, server 모두 동일
- config.properties.pc  local용 설정파일
- config.properties.prod 서버용 설정파일

config.properties파일들은 com.ontide.oneplanner.ctrl.Properties.java에 지정을 한다.
개발시 local실행할때는 *.pc, 서버 배포시에는 *.prod파일로 변경해서 build해야 한다.

2.1.3. spring 설정파일
src/main/webapp/WEB-INF/spring/root-context.xml 
=> 위의 config파일 지정
src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml
=> package명 지정
    
2.1.4. eclipse perspective설정
eclipse의 작업환경모드를 선택할수있는데 여기서는 전자정부프레임웍 모드를 선택한다.
Window > Open Perspective > Others.. > eGovFrame 선택
선택 후 좌측 하단 Servers창이 추가되는 것을 확인한다.
	
2.2. 프로젝트 빌드 및 실행
2.2.1. maven test
 project에서 필요로 하는 dependency library를 maven repository로 다운받음.
 프로젝트 선택 오른쪽 마우스 클릭후 > Run As> Maven test 

2.2.1. maven clean
 project에서 이전 build내용 cleanse처리
 프로젝트 선택 오른쪽 마우스 클릭후 > Run As > Maven clean 
 
2.2.2. maven build
 project를 build하여 war배포파일을 생성한다.
 프로젝트 선택 오른쪽 마우스 클릭후 > Run As > Maven build..
 선택창이 뜨는데 profiles를 prod로 입력 -> 실행 configuration을 명을 지정하고 저장한다. 
 빌드가 완료되면 target폴더 밑에 oneplanner-1.0.0.war 파일 생성을 확인한다.
 
2.2.3. 내장 webserver(tomcat v7.0)에 웹 등록
 웹을 내장tomcat을 통해 실행한다.
 프로젝트 선택 오른쪽 마우스 클릭후 > Run As > Run On Server
 하단 Servers창에 
 Tomcat v7.0 Server at localhost가 Running상태로 되고,
 oneplanner-web이 추가되어있는것을 확인한다.
 
2.2.4. Junit을 통한 기능테스트 실행
 내장tomcat에 설치된 web에 실행해볼 각 web api기능들을 다음과 같은 Junit파일으로 통해 실행 및 테스트 할수 있다.

src/test/java/com/ontide/oneplanner/client/UnitTest.java에 다음과 같은 @Test annotation지정된 메소드들을
메소드선택 우측마우스클릭 > Run As > Junit Test를 실행하면 해당 메소드만 실행된다. 

testFlowShort() - 기본 사용자등록 플로우 테스트
testFlowGoogleUser() -  구글 사용자 등록 플로우 테스트
testFlowNormal() - 전체 플로우  테스트
testFlowToday()  - Today 등록 테스트
testFlowTodaySimple() - Today 등록 테스트

> 서버에 배포된 웹을 테스트 하려면 
src/test/java/com/ontide/oneplanner/client/UnitObj.java의 URL변수를 변경해서 테스트한다.
 
2.3. 웹 서버 배포
maven build(profiles: prod)후 생성된 target폴더 밑에 oneplanner-1.0.0.war을 
WinSCP sftp로 서버 접속하여 /home/ubuntu/upload 경로로 upload한다.
(이하 서버 배포는 다음 3절에서 설명)

3.1. 서버 환경
putty를 통해 서버 접속하면 다음과 같은 폴더를 확인한다.
서버 버전: Ubuntu Linux 4.4.0 버전을 사용한다.
ubuntu@ip-172-31-1-254:~/shell$ uname -a
Linux ip-172-31-1-254 4.4.0-57-generic #78-Ubuntu SMP Fri Dec 9 23:50:32 UTC 2016 x86_64 x86_64 x86_64 GNU/Linux
ubuntu@ip-172-31-1-254:~/shell$

ubuntu@ip-172-31-1-254:~$ pwd
/home/ubuntu
ubuntu@ip-172-31-1-254:~$ ll backup  <=== war파일, db data 백업위치
total 84136
drwxrwxr-x 2 ubuntu ubuntu     4096 Apr 18 16:47 ./
drwxr-xr-x 7 ubuntu ubuntu     4096 Apr 19 01:07 ../
-rw-r--r-- 1 ubuntu ubuntu 12296011 Mar 29 13:09 oneplanner-1.0.0.war.20170329130919
-rw-r--r-- 1 ubuntu ubuntu 12307536 Apr 16 00:49 oneplanner-1.0.0.war.20170416004910
-rw-r--r-- 1 ubuntu ubuntu 12307268 Apr 16 14:55 oneplanner-1.0.0.war.20170416145552
-rw-r--r-- 1 ubuntu ubuntu 12307283 Apr 18 16:16 oneplanner-1.0.0.war.20170418161602
-rw-r--r-- 1 ubuntu ubuntu 12307846 Apr 18 16:23 oneplanner-1.0.0.war.20170418162318
-rw-r--r-- 1 ubuntu ubuntu 12308143 Apr 18 16:39 oneplanner-1.0.0.war.20170418163945
-rw-r--r-- 1 ubuntu ubuntu 12308304 Apr 18 16:47 oneplanner-1.0.0.war.20170418164748

** 서버에서 ubuntu계정은 제한되어있어 root계정실행 권한인 sudo 명령어를 사용한다.
(다음 스크립트파일 참고)
ubuntu@ip-172-31-1-254:~$ ll shell  <=== 각종 util 스크립트파일
total 84
drwxrwxr-x 2 ubuntu ubuntu  4096 Feb 27 16:09 ./
drwxr-xr-x 7 ubuntu ubuntu  4096 Apr 19 01:07 ../
-rw-rw-r-- 1 ubuntu ubuntu    92 Jan 16 14:31 backupdb.sh   <== db data 백업
-rw-rw-r-- 1 ubuntu ubuntu    36 Jan 17 14:11 check_mysql.sh <== mysql서버 상태확인 
-rw-rw-r-- 1 ubuntu ubuntu   463 Jan 19 14:04 deploy_war.sh  <== upload한 war파일을 배포
-rw-rw-r-- 1 ubuntu ubuntu    73 Feb 19 06:30 edit_mysqlconf.sh <== mysql 서버설정파일 my.cnf 편집
-rw-rw-r-- 1 ubuntu ubuntu    37 Jan 17 14:10 restart_mysql.old
-rw-rw-r-- 1 ubuntu ubuntu    29 Jan 17 14:10 restart_mysql.sh mysql재기동(사용할)
-rw-rw-r-- 1 ubuntu ubuntu    34 Jan 16 14:29 runmysql   <== mysql root console모드 실행
-rw-rw-r-- 1 ubuntu ubuntu 20480 Feb 27 15:54 shell.tar
-rw-rw-r-- 1 ubuntu ubuntu    35 Jan 17 14:10 start_mysql.old
-rw-rw-r-- 1 ubuntu ubuntu    27 Jan 17 14:09 start_mysql.sh  <== mysql 기동
-rw-rw-r-- 1 ubuntu ubuntu    33 Jan 16 14:29 start_tomcat.sh <== tomcat 기동/재기동
-rw-rw-r-- 1 ubuntu ubuntu    34 Jan 17 14:10 stop_mysql.old
-rw-rw-r-- 1 ubuntu ubuntu    26 Jan 17 14:10 stop_mysql.sh   <== mysql 종료
-rw-rw-r-- 1 ubuntu ubuntu    30 Jan 16 14:30 stop_tomcat.sh  <== tomcat 종료
-rw-rw-r-- 1 ubuntu ubuntu    90 Feb 17 11:36 tailtomcat.sh   <== tomcat 로그파일 tail
ubuntu@ip-172-31-1-254:~$ ll upload    <== 업로드 파일 위치
total 808728
drwxrwxr-x 2 ubuntu ubuntu      4096 Apr 24 09:11 ./
drwxr-xr-x 7 ubuntu ubuntu      4096 Apr 19 01:07 ../
-rw-rw-r-- 1 ubuntu ubuntu 815814817 Apr 24 07:57 eGovFrameDev-3.5.1-64bit.zip
-rw-rw-r-- 1 ubuntu ubuntu  12308444 Apr 18 16:47 oneplanner-1.0.0.war
ubuntu@ip-172-31-1-254:~$


ubuntu@ip-172-31-1-254:~/shell$ ll /datafile  <== 각종 datafile 위치 
total 12
drwxrwxrwx  3 root   root   4096 Apr 17 21:23 ./
drwxr-xr-x 24 root   root   4096 Apr 25 03:39 ../
drwxrwxrwx  4 ubuntu ubuntu 4096 Apr 19 06:16 today/  <== today이미지 파일 저장 (com.ontide.oneplanner.service.FileService확인)
ubuntu@ip-172-31-1-254:~/shell$

3.2. tomcat 설정
tomcat7버전으로 설치되어있다.

tomcat7 binary가 설치된 위치
ubuntu@ip-172-31-1-254:~/shell$ ll /usr/share/tomcat7/bin
total 108
drwxr-xr-x 2 root root  4096 Apr 18 16:41 ./
drwxr-xr-x 4 root root  4096 Feb 27 16:22 ../
-rw-r--r-- 1 root root 27111 Jun 27  2016 bootstrap.jar
-rwxr-xr-x 1 root root 20806 Jun 27  2016 catalina.sh*
-rw-r--r-- 1 root root  1647 Feb 10  2015 catalina-tasks.xml
-rwxr-xr-x 1 root root  1922 Jan 25  2014 configtest.sh*
-rwxr-xr-x 1 root root  7888 Jul  7  2014 daemon.sh*
-rwxr-xr-x 1 root root  1965 Jan 25  2014 digest.sh*
-rwxr-xr-x 1 root root  3547 Aug 11  2014 setclasspath.sh*
-rw-r--r-- 1 root root   305 Apr 18 16:41 setenv.sh   <== 필요 tomcat java option을 설정
-rwxr-xr-x 1 root root  1902 Jan 25  2014 shutdown.sh*
-rwxr-xr-x 1 root root  1904 Jan 25  2014 startup.sh*
lrwxrwxrwx 1 root root    26 Jun 27  2016 tomcat-juli.jar -> ../../java/tomcat-juli.jar
-rwxr-xr-x 1 root root  5024 Jan 25  2014 tool-wrapper.sh*
-rwxr-xr-x 1 root root  1908 Jan 25  2014 version.sh*

tomcat7 설정및 환경이 설치된 위치
ubuntu@ip-172-31-1-254:~/shell$ ll /var/lib/tomcat7/
total 24
drwxr-xr-x  6 root    root    4096 Feb 27 16:22 ./
drwxr-xr-x 48 root    root    4096 Mar  1 06:30 ../
drwxr-xr-x  3 tomcat7 tomcat7 4096 Feb 27 16:22 common/
lrwxrwxrwx  1 root    root      12 Jun 27  2016 conf -> /etc/tomcat7/
lrwxrwxrwx  1 root    root      17 Jun 27  2016 logs -> ../../log/tomcat7/
drwxr-xr-x  3 tomcat7 tomcat7 4096 Feb 27 16:22 server/
drwxr-xr-x  3 tomcat7 tomcat7 4096 Feb 27 16:22 shared/
drwxrwxr-x  6 tomcat7 tomcat7 4096 Apr 18 16:47 webapps/
lrwxrwxrwx  1 root    root      19 Jun 27  2016 work -> ../../cache/tomcat7/

ubuntu@ip-172-31-1-254:~/shell$ ll /var/lib/tomcat7/conf/*   <== conf파일 위치
-rw-r--r-- 1 root tomcat7   6506 Jun 27  2016 /var/lib/tomcat7/conf/catalina.properties
-rw-r--r-- 1 root tomcat7   1394 Jan 25  2014 /var/lib/tomcat7/conf/context.xml
-rw-r--r-- 1 root tomcat7   2370 Feb 18  2016 /var/lib/tomcat7/conf/logging.properties 
<==log4j설정파일 oneplanner.log로 지정

-rw-r--r-- 1 root tomcat7   6814 Apr 17 21:21 /var/lib/tomcat7/conf/server.xml   <== 기본 설정파일
-rw-r----- 1 root tomcat7   1605 Feb 27 16:29 /var/lib/tomcat7/conf/tomcat-users.xml  <== 웹관리툴 권환 설정파일
-rw-r--r-- 1 root tomcat7 168099 Nov 25  2015 /var/lib/tomcat7/conf/web.xml

ubuntu@ip-172-31-1-254:~/shell$ ll /var/lib/tomcat7/logs/*
-rw-r--r-- 1 tomcat7 tomcat7    12876 Apr 27 05:11 /var/lib/tomcat7/logs/catalina.2017-04-27.log
-rw-r--r-- 1 tomcat7 root       55009 Apr 27 05:11 /var/lib/tomcat7/logs/catalina.out <==톰캣 기본 로그파일
-rw-r--r-- 1 tomcat7 tomcat7      860 Feb 27 21:49 /var/lib/tomcat7/logs/localhost_access_log.2017-04-27.txt
-rw-r--r-- 1 tomcat7 tomcat7    53704 Apr 27 01:58 /var/lib/tomcat7/logs/oneplanner.log <== web 모듈 로그
ubuntu@ip-172-31-1-254:~/shell$ ll /var/lib/tomcat7/server/*
total 8
drwxr-xr-x 2 tomcat7 tomcat7 4096 Jun 27  2016 ./
drwxr-xr-x 3 tomcat7 tomcat7 4096 Feb 27 16:22 ../
ubuntu@ip-172-31-1-254:~/shell$ ll /var/lib/tomcat7/webapps/*
-rw-r--r-- 1 tomcat7 tomcat7    28530 Mar  3 06:38 /var/lib/tomcat7/webapps/6h6.war
-rw-r--r-- 1 tomcat7 tomcat7     4697 Mar 15 17:26 /var/lib/tomcat7/webapps/host-manage.war
-rw-r--r-- 1 root    root    12308444 Apr 18 16:47 /var/lib/tomcat7/webapps/oneplanner-1.0.0.war <==web 배포파일

/var/lib/tomcat7/webapps/oneplanner-1.0.0:  <==war파일을 배포하면 추출됨
total 16
drwxr-xr-x 4 tomcat7 tomcat7 4096 Apr 18 16:47 ./
drwxrwxr-x 6 tomcat7 tomcat7 4096 Apr 18 16:47 ../
drwxr-xr-x 3 tomcat7 tomcat7 4096 Apr 18 16:47 META-INF/
drwxr-xr-x 6 tomcat7 tomcat7 4096 Apr 18 16:47 WEB-INF/

3.3. web배포
2.3절에서 upload된 war파일은 다음과 같이 배포한다.
cd ~/shell
bash deploy_war.sh
==> war 파일 /var/lib/tomcat7/webapps/위치에 배포확인할것
bash start_tomcat.sh
==> 프로세스 재기동 확인
web server 로그 확인
bash tailtomcat.sh

3.4. tomcat 기동확인
ubuntu@ip-172-31-1-254:~/shell$ ps -ef | grep java
tomcat7   5352     1  0 Apr19 ?        00:10:09 /usr/lib/jvm/default-java/bin/java -Djava.util.logging.config.file=/var/lib/tomcat7/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Dfile.encoding=UTF-8 -Xms128m -Xmx1024m -XX:PermSize=64m -XX:MaxPermSize=256m -XX:+CMSClassUnloadingEnabled -XX:ParallelGCThreads=2 -XX:-UseConcMarkSweepGC -XX:-PrintGC -XX:-PrintGCDetails -XX:-PrintGCTimeStamps -XX:-TraceClassUnloading -XX:-TraceClassLoading -Dwork.path=/datafile/ -Djava.endorsed.dirs=/usr/share/tomcat7/endorsed -classpath /usr/share/tomcat7/bin/bootstrap.jar:/usr/share/tomcat7/bin/tomcat-juli.jar -Dcatalina.base=/var/lib/tomcat7 -Dcatalina.home=/usr/share/tomcat7 -Djava.io.tmpdir=/tmp/tomcat7-tomcat7-tmp org.apache.catalina.startup.Bootstrap start
ubuntu   31982 31350  0 08:27 pts/0    00:00:00 grep --color=auto java
ubuntu@ip-172-31-1-254:~/shell$

3.5. Mysql 설정
3.5.1. Mysql상태확인
ubuntu@ip-172-31-1-254:~/shell$ bash check_mysql.sh
● mysql.service - MySQL Community Server
   Loaded: loaded (/lib/systemd/system/mysql.service; enabled; vendor preset: enabled)
   Active: active (running) since Mon 2017-02-27 16:20:07 UTC; 1 months 28 days ago
 Main PID: 12792 (mysqld)
    Tasks: 32
   Memory: 165.7M
      CPU: 21min 41.639s
   CGroup: /system.slice/mysql.service
           └─12792 /usr/sbin/mysqld

Warning: Journal has been rotated since unit was started. Log output is incomplete or unavailable.
ubuntu@ip-172-31-1-254:~/shell$

3.5.2. Mysql기동 
cd ~/shell
bash start_mysql.sh 또는 restart_mysql.sh

3.5.2. Mysql종료 
cd ~/shell
bash stop_mysql.sh

3.5.3. mysql console접속
ubuntu@ip-172-31-1-254:~/shell$ bash runmysql
mysql: [Warning] Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 200
Server version: 5.7.17-0ubuntu0.16.04.1 (Ubuntu)

Copyright (c) 2000, 2016, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>

참고> Mysql 5.7 서버 설정
처음에는 runtime상에서 set global.. 로 하나씩 설정할것
select @@innodb_buffer_pool_instances;
select @@innodb_buffer_pool_chunk_size ;
select @@innodb_buffer_pool_size/1024/1024;
set global innodb_buffer_pool_size = 134217728; -- 128M default
select @@innodb_log_file_size/1024/1024;  
set global innodb_buffer_pool_size = 50331648; -- 48M default
select @@max_connections;  -- 151 default 
select @@innodb_flush_log_at_trx_commit;
select @@innodb_flush_method; -- linux onley O_DIRECT
select @@innodb_log_buffer_size/1024/1024;  -- 
set global innodb_log_buffer_size = 1048576; -- 1M default desirable
select @@query_cache_size;  -- 0 desirable
   
   lock발생 등 상태 확인
* Check InnoDB status for locks
SHOW ENGINE InnoDB STATUS;

* Check MySQL open tables
SHOW OPEN TABLES WHERE In_use > 0;

* Check pending InnoDB transactions
SELECT * FROM `information_schema`.`innodb_trx` ORDER BY `trx_started`; 

* Check lock dependency - what blocks what
SELECT * FROM `information_schema`.`innodb_locks`;



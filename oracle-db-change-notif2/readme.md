Презентация Oracle Data change captute - notify
===============================================

* [Сама презентация](https://github.com/gochaorg/samples/blob/master/oracle-db-change-notif2/doc/pres.pdf)
* [Исходный код тут :)](https://github.com/gochaorg/samples/tree/master/oracle-db-change-notif2)

Ресурсы / ссылки

* [Презентация ORACLE](http://www.oraclebi.ru/files/presentations/imelnikov/ChangeNotification.pdf)
* [Database JDBC Developer's Guide / Database Change Notification](https://docs.oracle.com/cd/E11882_01/java.112/e16548/dbchgnf.htm#JJDBC28815)
* [dbms_change_notification Tips](http://www.dba-oracle.com/t_packages_dbms_change_notification.htm)
* [DBMS_CHANGE_NOTIFICATION](https://docs.oracle.com/cd/B19306_01/appdev.102/b14258/d_chngnt.htm#BABEECBE)
* [Developing Applications with Database Change Notification, Best Practices, Troubleshooting](https://docs.oracle.com/cd/B19306_01/B14251_01/adfns_dcn.htm#ADFNS1020)

По исходному коду
-----------------

Данный код представляет пример клиента СУБД написанного java (версия 8) который слушает события изменения данных

Пример демонстрировался в следующих условиях

* ОС - Linux - Ubuntu 18
* База данных [Oracle 12c в контейнере docker](https://hub.docker.com/_/oracle-database-enterprise-edition)
* Среда разработки [idea community](https://www.jetbrains.com/ru-ru/idea/)
* Система сборки maven 3.6
* [Bellsoft JDK 14 (подойдет и 8)](https://bell-sw.com/pages/java-14/)

Чтоб воспроизвести пример необходимо выполнить следующие шаги

1. Иметь предустановленную СУБД, самый легкий путь - поставить под linux + docker
2. Иметь средства разработки (jdk, maven, idea)
3. Запустить контейнер oracle docker, см команду ниже
4. [Создать пользователя в СУБД](https://github.com/gochaorg/samples/blob/master/oracle-db-change-notif2/src/main/resources/create_user.sql)
5. [От имени созданного пользователя создать таблицу и первоначальные данные](https://github.com/gochaorg/samples/blob/master/oracle-db-change-notif2/src/main/resources/create_table.sql), комментарии см ниже
6. Открыть проект в idea и запустить [тест](https://github.com/gochaorg/samples/blob/master/oracle-db-change-notif2/src/test/java/xyz/cofe/sample/oranotif/OraNotifMainTest.java)
7. Изменить данные в исходной таблице ORACLE, комментарии см ниже

### Команда запуска контейнера oracle

Кратная команда

```
docker run --name=ora1 -p 1521:1521 -d -it store/oracle/database-enterprise:12.2.0.1 
```

Полная команда

```bash
docker run --name=ora1 --hostname=1581b47a47ab --user=oracle --env="PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin" --env="ORACLE_HOME=/u01/app/oracle/product/12.2.0/dbhome_1" --env="ORACLE_SID=ORCL" --volume="/ORCL" -p 1521:1521 --restart=no --detach=true -t store/oracle/database-enterprise:12.2.0.1 /bin/sh -c '/bin/bash /home/oracle/setup/dockerInit.sh
```

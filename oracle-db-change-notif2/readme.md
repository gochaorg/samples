Презентация Oracle Data change captute - notify
===============================================

* [Сама презентация](https://github.com/gochaorg/samples/blob/master/oracle-db-change-notif2/doc/pres.pdf)
* [Исходный код nen :)](https://github.com/gochaorg/samples/tree/master/oracle-db-change-notif2)

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

Чтоб восспроизвести пример необходимо выполнить следующие шаги

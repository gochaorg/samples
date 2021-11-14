SQL - Serializable
====================

Режим изоляции SERIALIZABLE - очень мутная тема, данный проект преследует демонстрацию.

Подготовка
==================

1. Запуск mssql (docker)
2. Создание базы
3. Создание пользователей
4. Раздача прав пользователям
5. Создание тестовых таблиц

Создание базы и логинов
-------------------------

    1> create database test01;
    2> go
    1> create login usr1 with password = 'usr1' , check_policy = off , check_expiration = off , default_database = test01 ;
    2> go
    1> use test01;
    2> go
    Changed database context to 'test01'.
    1> create user usr1 for login usr1;
    2> go
    1> exec sp_addrolemember 'db_owner', 'usr1';
    2> go
    1> create login usr2 with password = 'usr2', check_policy = off , check_expiration = off , default_database = test01;
    2> go
    1> create user usr2 for login usr2;
    2> go
    1> exec sp_addrolemember 'db_owner', 'usr2';
    2> go
    1> grant view server state to usr1;
    2> go
    1> grant view server state to usr2;
    2> go

Создание таблицы
----------------------

    > ./connect_usr1.sh 
    1> SET IMPLICIT_TRANSACTIONS OFF;
    2> go
    1> create table t1 (
    2>   n int not null default (0),
    3>   t nvarchar(200) null
    4> );
    5> go
    1> 


Смена уровная изоляции

    SET TRANSACTION ISOLATION LEVEL
        { READ UNCOMMITTED
        | READ COMMITTED
        | REPEATABLE READ
        | SNAPSHOT
        | SERIALIZABLE
        }

Запрет неявных транзакций

    SET IMPLICIT_TRANSACTIONS OFF

Просмотр блокировок

    SELECT * FROM sys.dm_tran_locks

Колонки

RID
Если таблица сохранена как куча, отдельные строки идентифицируются по ссылке на 8-байтовый идентификатор строки (RID), состоящий из номера файла, номера страницы данных и слота на странице (FileID:PageID:SlotID). Идентификатор строки является небольшой и эффективной структурой.
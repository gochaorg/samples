Пример использования Oracle Change Notification
===============================================

Возможность уведомления клиентов о изменениях данных в БД
появилось в Oracle 11g

Подготовка
----------

### Выдать права пользователю oracle

```sql
GRANT CHANGE NOTIFICATION TO "TESTNOTIFY";
```

### Создать таблицу и наполнить данными

```sql
CREATE TABLE TABLE1
(
  ID NUMBER NOT NULL
, TXT VARCHAR2(200 BYTE)
, CONSTRAINT TABLE1_PK PRIMARY KEY
  (
    ID
  )
)
;

insert into table1 (txt) values ('line 1');
insert into table1 (txt) values ('line 2');
insert into table1 (txt) values ('line 3');
insert into table1 (txt) values ('line 4');
commit;
```

Код java
---------

Точно потребуются след классы
```sql
import java.util.Properties;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
```

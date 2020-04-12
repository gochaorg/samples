sample app of oracle data change listener
=========================================

syntax
------

  `ora-notif-listener` { key value }
 
where key:

* -host OracleIP
* -port OraclePort
* -service OracleServiceOrSID
* -user OracleUsername
* -password OraclePassword
* -lq SqlSelect
* -timeout N 
* -timeout N timeunit
* -echo echoPeriodTime
* -echo echoPeriodTime timeunit
* -cSI trueFalse
* -listenQueryInBG trueFalse
* -useExecSrvc trueFalse

where timeunit:

* ms 
* msec
* msecond
* mseconds
* s
* sec
* second
* seconds 

run sample
==========

    $ ora-notif-listener \
      -host localhost -port 1521 -service ORCLCDB.localdomain \
      -user TESTNOTIFY -password TESTNOTIFY \
      -lq 'SELECT * FROM TABLE1' \
      -timeout 60 sec \
      -cSI true

    wait for DatabaseChangeEvent, wait time=30 seconds, max=60
    DatabaseChangeEvent:
    Connection information  : local=172.17.0.1/172.17.0.1:47632, remote=172.17.0.2/172.17.0.2:59138
    Registration ID         : 607
    Notification version    : 1
    Event type              : QUERYCHANGE
    Database name           : ORCLCDB
    Query Change Description (length=1)
      query ID=42, query change event type=QUERYCHANGE
      Table Change Description (length=1):    operation=[DELETE], tableName=TESTNOTIFY.TABLE1, objectNumber=77789
        Row Change Description (length=2):
          ROW:  operation=DELETE, ROWID=AAAS/dAAHAAAACFAAA
          ROW:  operation=DELETE, ROWID=AAAS/dAAHAAAACFAAB
    
    wait for DatabaseChangeEvent, wait time=48 seconds, max=60
    DatabaseChangeEvent:
    Connection information  : local=172.17.0.1/172.17.0.1:47632, remote=172.17.0.2/172.17.0.2:59138
    Registration ID         : 607
    Notification version    : 1
    Event type              : QUERYCHANGE
    Database name           : ORCLCDB
    Query Change Description (length=1)
      query ID=42, query change event type=QUERYCHANGE
      Table Change Description (length=1):    operation=[INSERT], tableName=TESTNOTIFY.TABLE1, objectNumber=77789
        Row Change Description (length=1):
          ROW:  operation=INSERT, ROWID=AAAS/dAAHAAAACFAAC

About
====================

Простой сервис, для тестирования docker

Docker
==================

build
---------

run
---------

    docker run -p 8080:4567 test:net

Rest API
===================

POST /note/{name} Создание/обновление заметки
-----------------------------------------------

    🚀 curl -v -X POST -d 'blabla' http://localhost:8080/note/n1

    Note: Unnecessary use of -X or --request, POST is already inferred.
    *   Trying 127.0.0.1:8080...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > POST /note/n2 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.81.0
    > Accept: */*
    > Content-Length: 6
    > Content-Type: application/x-www-form-urlencoded
    >
    * Mark bundle as not supporting multiuse
    < HTTP/1.1 200 OK
    < Date: Wed, 31 Jan 2024 20:09:38 GMT
    < Content-Type: text/html;charset=utf-8
    < Transfer-Encoding: chunked
    < Server: Jetty(9.4.48.v20220622)
    <
    * Connection #0 to host localhost left intact
    ok

GET /note Список заметок
-----------------------------------------------

    🚀 curl -v http://localhost:8080/note | jq

```json
{
  "notes": [
    "n1",
    "n2"
  ]
}
```

GET /note/{name} Чтение заметки
-----------------------------------------------

    🚀 curl -v -w "\n" http://localhost:8080/note/n1
    *   Trying 127.0.0.1:8080...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > GET /note/n1 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.81.0
    > Accept: */*
    >
    * Mark bundle as not supporting multiuse
    < HTTP/1.1 200 OK
    < Date: Wed, 31 Jan 2024 20:18:45 GMT
    < Content-Type: text/html;charset=utf-8
    < Transfer-Encoding: chunked
    < Server: Jetty(9.4.48.v20220622)
    <
    * Connection #0 to host localhost left intact
    blabla

DELETE /note/{name} Удаление заметки
--------------------------------------

    🚀 curl -v -w "\n" -X DELETE http://localhost:8080/note/n1
    *   Trying 127.0.0.1:8080...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > DELETE /note/n1 HTTP/1.1
    > Host: localhost:8080
    > User-Agent: curl/7.81.0
    > Accept: */*
    >
    * Mark bundle as not supporting multiuse
    < HTTP/1.1 200 OK
    < Date: Wed, 31 Jan 2024 20:17:30 GMT
    < Content-Type: text/html;charset=utf-8
    < Transfer-Encoding: chunked
    < Server: Jetty(9.4.48.v20220622)
    <
    * Connection #0 to host localhost left intact
    ok
